#!/bin/bash
cd /home/user/tibco
sed -i -e 's/localhost/'$PUBLIC_IP'/g' /home/user/tibco/tibco/cfgmgmt/ems/data/factories.conf
/opt/tibco/ems/8.3/bin/tibemsd64.sh