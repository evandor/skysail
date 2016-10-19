#!/bin/bash -e

##########################################################################
### Deployment Script SSP Demo Integration  ##############################
##########################################################################

### CONFIGURATION ########################################################
echo ""
echo "Script Configuration:"
echo "---------------------"

APPNAME="demo"
STAGE="int"

MAIN_DEPLOY_SCRIPT=../../../skysail.server/deployment/scripts/deploy.sh
echo "calling \$MAIN_DEPLOY_SCRIPT"

chmod 775 \$MAIN_DEPLOY_SCRIPT
source \$MAIN_DEPLOY_SCRIPT

