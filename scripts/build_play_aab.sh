#!/usr/bin/env bash
# Build the Google Play upload artifact (release-signed universal AAB).
# The upload key lives outside git in app/keystore/, with its passwords in
# keystore.properties at the repository root.
set -euo pipefail
cd "$(dirname "$0")/.."

if [ ! -f keystore.properties ]; then
    echo "keystore.properties is missing. See PLAY_INTERNAL_TESTING.md." >&2
    exit 1
fi
if [ ! -f app/keystore/release.keystore ]; then
    echo "app/keystore/release.keystore is missing. See PLAY_INTERNAL_TESTING.md." >&2
    exit 1
fi

GRADLE_ARGS=(bundleUniversalFossPlay --console=plain)
if [ -n "${PLAY_VERSION_CODE:-}" ]; then
    case "$PLAY_VERSION_CODE" in
        *[!0-9]*|'')
            echo "PLAY_VERSION_CODE must be a whole number." >&2
            exit 1
            ;;
    esac
    GRADLE_ARGS+=("-PversionCode=$PLAY_VERSION_CODE")
fi

./gradlew "${GRADLE_ARGS[@]}"

AAB=$(find app/build/outputs/bundle/universalFossPlay -maxdepth 1 -name '*.aab' -print -quit)
if [ -z "$AAB" ] || [ ! -f "$AAB" ]; then
    echo "No AAB found under app/build/outputs/bundle/universalFossPlay." >&2
    exit 1
fi

mkdir -p apk-generated
SUFFIX="${PLAY_VERSION_CODE:-current}"
OUT="apk-generated/DOOM-Play-b${SUFFIX}.aab"
cp "$AAB" "$OUT"

echo
echo "Play upload artifact: $OUT"
sha256sum "$OUT"
