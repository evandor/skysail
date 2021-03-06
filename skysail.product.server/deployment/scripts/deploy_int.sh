#!/bin/bash -e

##########################################################################
### Deployment Script SSP pline Integration  ##############################
##########################################################################

### CONFIGURATION ########################################################
echo ""
echo "Script Configuration:"
echo "---------------------"

PROJECTNAME="skysail.product.server"
STAGE="int"

MAIN_DEPLOY_SCRIPT=../../../skysail.server/deployment/scripts/deploy.sh
echo "calling $MAIN_DEPLOY_SCRIPT"

chmod 775 $MAIN_DEPLOY_SCRIPT
source $MAIN_DEPLOY_SCRIPT

cd /home/carsten/skysail/products/server/int/bin
mkdir -p plugins
