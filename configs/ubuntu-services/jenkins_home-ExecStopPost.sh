#!/bin/bash

set -o nounset -o errexit
PATH=/sbin:/bin:/usr/sbin:/usr/bin
unalias -a

# Remove all of Apache's configurations and restart Apache.
rm -f /etc/apache2/sites-enabled/*.jenkins_home.conf.subdomain
service apache2 reload || true
