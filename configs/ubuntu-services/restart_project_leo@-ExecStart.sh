#!/bin/bash

set -o nounset -o errexit
PATH=/sbin:/bin:/usr/sbin:/usr/bin
unalias -a

if [ $# != 1 ]; then
  echo "Usage: systemctl start project_leo_restart@\${USER}.service"
  exit 1
fi

# We only need to kill the process. The loop in the project_leo.service
# will restart it automatically.
USER="${1}"
PORT="$(cat "/home/${USER}/.project_leo/run/${USER}/server.port")"
pkill --euid "${USER}" --full -- \
    "-jar [^ ]+/project-leo-server-.* --server.port=${PORT}"
