#!/bin/bash -e

##########################################################################
### Deployment Script SSP Pact Integration  ##############################
##########################################################################

### CONFIGURATION ########################################################
echo ""
echo "Script Configuration:"
echo "---------------------"

APPNAME="pact"
STAGE="int"

MAIN_DEPLOY_SCRIPT_PATH=../../../skysail.server/deployment/scripts

echo "calling $MAIN_DEPLOY_SCRIPT_PATH/deploy.sh"
chmod 775 $MAIN_DEPLOY_SCRIPT_PATH/deploy.sh
source $MAIN_DEPLOY_SCRIPT_PATH/deploy.sh

echo "calling $MAIN_DEPLOY_SCRIPT_PATH/startService.sh"
chmod 775 $MAIN_DEPLOY_SCRIPT_PATH/startService.sh
source $MAIN_DEPLOY_SCRIPT_PATH/startService.sh
