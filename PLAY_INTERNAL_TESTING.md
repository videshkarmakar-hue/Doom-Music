# DOOM on Google Play: internal testing setup

This is the Play distribution path. Installing an APK outside Play makes Google Play Protect
show the "App scan recommended" prompt; installing the same app from a Play testing track
does not. No app-side code can suppress that prompt — Play distribution is the fix.

The Play build is the `play` build type with the `foss` flavor:

- release-signed, never the debug key
- no `REQUEST_INSTALL_PACKAGES` permission and no in-app APK installer
- no `MANAGE_EXTERNAL_STORAGE` permission; downloads/cache use app-owned storage
- package `com.videshkarmakar.doom` (not the `.debug` package)
- update screens point at Google Play instead of GitHub

## 1. Back up the upload key first

The repository now contains a stable upload keystore created on this machine:

- `app/keystore/release.keystore` — the upload key
- `keystore.properties` — its passwords and alias

Both are gitignored. Copy them to a password manager or an encrypted backup **before**
uploading anything. Losing this key means requesting an upload-key reset from Google.

Do not paste the passwords into chat, commit them, or put them in CI logs. The Gradle build
reads `keystore.properties`; CI can instead provide the same values as environment variables:

| Property | Environment variable |
| --- | --- |
| `storeFile` | `STORE_FILE` |
| `storePassword` | `STORE_PASSWORD` |
| `keyAlias` | `KEY_ALIAS` |
| `keyPassword` | `KEY_PASSWORD` |

`STORE_FILE` may be absolute, or relative to the repository root. The default is
`app/keystore/release.keystore`.

## 2. Build the Play AAB

```bash
PLAY_VERSION_CODE=1271 ./scripts/build_play_aab.sh
```

The script runs `bundleUniversalFossPlay` and copies the result to
`apk-generated/DOOM-Play-b<versionCode>.aab`. It prints the SHA-256.

Every Play upload needs a **higher `versionCode`** than the previous one. The app's current
default is `1271`; use `PLAY_VERSION_CODE=1272` for the next upload, and so on. `versionName`
can stay the same or be overridden with `-PversionName=...` on the Gradle command line.

Before uploading, verify:

```bash
# The AAB is release-signed (Gradle prints the certificate when verifying).
jarsigner -verify -verbose -certs apk-generated/DOOM-Play-b1271.aab | head -n 20

# The Play merged manifest must not contain either permission.
grep -R "MANAGE_EXTERNAL_STORAGE\|REQUEST_INSTALL_PACKAGES" \
  app/build/intermediates/merged_manifests/universalFossPlay
```

The grep should print nothing for a Play build.

## 3. Create the Play Console app

1. Register a Google Play developer account at <https://play.google.com/console> and pay the
   one-time registration fee.
2. Create the app:
   - App name: `DOOM` (or the store name you want)
   - Default language, app or game, free/paid
   - Package name: `com.videshkarmakar.doom` — it must match the AAB exactly
3. Complete **Play App Signing**:
   - Let Google manage the app signing key.
   - Upload key: the `release.keystore` generated above.
   - Google signs the installed app; you keep using the upload key for future AABs.

## 4. Complete the required app content

Play will not let you publish until these are filled in:

- **Store listing**: app name, short description, full description, 512×512 icon,
  1024×500 feature graphic, phone screenshots, and TV screenshots if you ship Android TV.
- **Privacy policy**: a public URL. It must cover account/profile data, listening history,
  Last.fm/Libre.fm/Discord/YouTube integrations, downloads, and any analytics or crash data.
- **Data safety**: declare what is collected, whether it is shared, whether it is encrypted in
  transit, and whether users can request deletion.
- **Content rating** questionnaire.
- **Ads**: declare whether the app contains ads.
- **Target audience**: select the age groups that apply.
- **News app / COVID-19 / financial features**: answer the declarations that apply.
- **Government apps**: normally not applicable.
- **Data deletion**: provide a deletion route if you collect accounts or personal data.

Any mismatch between the privacy policy, Data safety form, and what the app actually does can
block review.

## 5. Create the internal testing release

1. In Play Console, open **Testing → Internal testing**.
2. Create a new release.
3. Upload the AAB from step 2.
4. Add release notes (for example: `First Play internal test`).
5. Save, review, and start the rollout.
6. Open the **Testers** tab and create an email list. Add the Google accounts that should test
   the app. Internal testing supports up to 100 testers.
7. Copy the opt-in URL and send it to the testers. They must open it, opt in, and then install
   DOOM from the Play Store link.

Install from the Play link, not from a sideloaded APK. That is the part that avoids the Play
Protect scan prompt.

## 6. Verify the test install

On a tester device:

1. Uninstall the old sideloaded DOOM build first if its signature differs. Android will refuse
   an update signed by a different key.
2. Open the opt-in URL and accept the test.
3. Install from Play.
4. Confirm Play Protect does not show the scan prompt during install.
5. Open the app and check that Settings → App update says updates are delivered by Google Play.
6. Check downloads and playback still work with app-owned storage.

## 7. Move toward production

Internal testing is enough to install without the scan prompt, but production is a separate
rollout. After the internal test looks good:

1. Promote the same tested AAB to **Closed testing** or **Open testing**, then production.
2. Personal developer accounts created after 13 November 2023 must run a closed test with at
   least 12 testers for 14 continuous days before applying for production access. Check the
   current rule in Play Console because Google changes these requirements.
3. Keep increasing `versionCode` for every uploaded AAB.

## Policy notes specific to this app

### All-files storage is removed from the Play build

The sideloaded app declared `MANAGE_EXTERNAL_STORAGE` so it could write to a folder chosen with
the system folder picker. Google Play restricts that permission to file managers, backup tools,
and similar core file-management apps. A music player is normally not eligible. The Play build
therefore removes the permission and hides the "Choose folder" row; downloads and cache stay in
DOOM's own app-specific storage.

If custom folders are essential for Play users, the storage layer must be rewritten to use the
Storage Access Framework (`DocumentFile`/`ContentResolver`) end to end instead of converting the
picked tree to a `java.io.File`. Do not re-add `MANAGE_EXTERNAL_STORAGE` without checking the
current Play policy and the declaration form.

### Self-update is removed from the Play build

`REQUEST_INSTALL_PACKAGES` is removed and the in-app GitHub updater is disabled. Play policy
does not allow an app distributed on Play to download and install its own APK updates. The Play
build shows Play guidance in Settings → App update instead.

The sideload/F-Droid builds keep their existing behavior; this only applies to the `play`
variant.

### Signing and updates

- The first Play upload can use the existing `versionCode` 1271. Every later upload must use a
  higher number.
- If the upload key is lost, use Play Console's upload-key reset flow. Keep the keystore and
  `keystore.properties` backed up.
- The debug package (`com.videshkarmakar.doom.debug`) is not the Play package and cannot be
  uploaded as an update to the Play app.

## Troubleshooting

**`Play builds need stable signing`** — `keystore.properties` or
`app/keystore/release.keystore` is missing. Restore the backup, or create a new upload key and
enroll it in Play App Signing.

**`keytool` not found** — install a JDK and make sure `keytool` is on `PATH`, or set
`JAVA_HOME` to the JDK used by Gradle.

**Play rejects `MANAGE_EXTERNAL_STORAGE`** — the Play manifest should already remove it. Check
the merged manifest and make sure you uploaded the `universalFossPlay` AAB, not a debug or
regular release AAB.

**"App not installed" when updating a sideloaded build** — the Play build is signed by Google
Play App Signing, so its signature differs from the local debug/upload build. Uninstall the
sideloaded copy first, then install from Play.

**Play Protect still warns** — make sure the device installed the app from the Play opt-in link,
not from an APK file or a browser download. Testers sometimes have an old APK still installed.
