JOB_DIR="/home/carsten/.hudson/jobs/ssp.$APPNAME.export.$STAGE/workspace/skysail.product.$APPNAME"

cd $JOB_DIR/generated/distributions/executable

### PUBLIC SITE ##########################################################
echo ""
echo "Manage public site:"
echo "-------------------"

echo "copying skysail.$APPNAME.zip to public site"
mkdir -p /var/www/skysail.io/public_html/skysail/products/$APPNAME
#cp skysail.$APPNAME.zip /var/www/downloads.skysail.io/skysail/products/$APPNAME/skysail.$APPNAME.$STAGE.zip
cp skysail.$APPNAME.jar /var/www/downloads.skysail.io/skysail/products/$APPNAME/skysail.$APPNAME.$STAGE.jar
