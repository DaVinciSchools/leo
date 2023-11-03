#!/bin/bash

set -o nounset -o errexit
PATH=/sbin:/bin:/usr/sbin:/usr/bin
unalias -a

# Remove all of Apache's configurations and restart Apache.
rm -f /etc/apache2/sites-enabled/*.jenkins_home.conf.subdomain
service apache2 reload || true

# Require jenkins to be installed.
[ ! -e /usr/share/java/jenkins.war ] && exit 1

# Iterate through each home directory and see if they have a file
# named ".run_jenkins". For each, start Jenkins.
PIDS=""
for HOME_DIR in /home/*; do

  [ ! -f "${HOME_DIR}/.run_jenkins" ] && continue;

  HTTPS="$([ "$(hostname -f)" = projectleo.net ] && echo https || echo http)"
  USER="$(basename "${HOME_DIR}")"
  PORT="$(/root/bin/get-open-port)"
  SUBDOMAIN=www

  # shellcheck disable=SC2211
  HTTPS="${HTTPS}" \
  PORT="${PORT}" \
  USER="${USER}" \
  SUBDOMAIN="${SUBDOMAIN}" \
      /etc/apache2/sites-available/??-jenkins_home.template.sh \
      > "/etc/apache2/sites-enabled/${SUBDOMAIN}.jenkins_home.conf.subdomain"

  HTTPS="${HTTPS}" \
  SUBDOMAIN="${SUBDOMAIN}" \
      /etc/apache2/sites-available/virtual_host.template.sh \
      > "/etc/apache2/sites-enabled/${SUBDOMAIN}.virtual_host.conf"

  while true; do
    umask 027
    sudo -H -u "${USER}" \
      /usr/bin/nice \
      /usr/bin/ionice -c 3 \
      /usr/bin/java \
      -Djava.awt.headless=true \
      -Dhudson.plugins.git.GitSCM.ALLOW_LOCAL_CHECKOUT=true \
      -jar /usr/share/java/jenkins.war \
      --httpPort="${PORT}" \
      --prefix=/~"${USER}"/jenkins
    echo Restarting Jenkins for user '${USER}'.
    sleep 5
  done &
  PIDS="${PIDS},$!"
done

# Restart Apache with the new configurations.
service apache2 reload

# shellcheck disable=SC2064
trap "pkill -P ${PIDS:1}; exit 0" TERM
wait ${PIDS//,/ }
