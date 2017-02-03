#!/bin/bash -e

##########################################################################
### Deployment Script SSP Desinger Integration ###########################
##########################################################################

### CONFIGURATION ########################################################
echo ""
echo "Script Configuration:"
echo "---------------------"

APPNAME="prototypr"
STAGE="int"

MAIN_DEPLOY_SCRIPT=/home/carsten/.hudson/jobs/skysail/workspace/skysail.server/deployment/scripts/deploy.sh
echo "calling $MAIN_DEPLOY_SCRIPT"

chmod 775 $MAIN_DEPLOY_SCRIPT
source $MAIN_DEPLOY_SCRIPT

cd /home/carsten/skysail/products/prototypr/int/bin
mkdir -p designerbundles

