#!/bin/bash -x
set -e
. "$(dirname "${BASH_SOURCE[0]}")/common.sh"

echo "Executing 'mvn clean package'."
mvn --file "${STAGED_FILES_TEMP_DIR}/pom.xml" clean package
if [ $? -ne 0 ]; then
  echo "Maven failed. Please run 'mvn package' locally and commit the fixes."
  exit 1
fi
