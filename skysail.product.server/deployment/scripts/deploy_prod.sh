#!/bin/bash -e

##########################################################################
### Deployment Script SSP pline Prod         ##############################
##########################################################################

### CONFIGURATION ########################################################
echo ""
echo "Script Configuration:"
echo "---------------------"

PROJECTNAME="skysail.product.server"
STAGE="prod"

MAIN_DEPLOY_SCRIPT=../../../skysail.server/deployment/scripts/deploy.sh
echo "calling $MAIN_DEPLOY_SCRIPT"

chmod 775 $MAIN_DEPLOY_SCRIPT
source $MAIN_DEPLOY_SCRIPT

cd /home/carsten/skysail/products/server/prod/bin
mkdir -p plugins
