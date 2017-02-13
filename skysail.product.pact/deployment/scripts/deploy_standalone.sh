#!/bin/bash -e

##########################################################################
### Deployment Script SSP Pact Integration  ##############################
##########################################################################

CURRENT_DIR=`pwd`

### CONFIGURATION ########################################################
echo ""
echo "Script Configuration:"
echo "---------------------"
echo ""
echo "PWD: $CURRENT_DIR"
echo ""

APPNAME="pact"
STAGE="standalone"

MAIN_DEPLOY_SCRIPT_PATH=../../../skysail.server/deployment/scripts

cd $CURRENT_DIR
echo "calling $MAIN_DEPLOY_SCRIPT_PATH/deploy.sh"
chmod 775 $MAIN_DEPLOY_SCRIPT_PATH/deploy.sh
source $MAIN_DEPLOY_SCRIPT_PATH/deploy.sh

cd $CURRENT_DIR
echo "calling $MAIN_DEPLOY_SCRIPT_PATH/publish.sh"
chmod 775 $MAIN_DEPLOY_SCRIPT_PATH/publish.sh
source $MAIN_DEPLOY_SCRIPT_PATH/publish.sh
