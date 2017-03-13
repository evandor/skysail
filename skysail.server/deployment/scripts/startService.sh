JOB_DIR="/home/carsten/.hudson/jobs/ssp.$APPNAME.export.$STAGE/workspace/$PROJECTNAME"
PRODUCT_DIR="/home/carsten/skysail/products/$APPNAME/$STAGE"
SERVICENAME=${APPNAME}_${STAGE}
export JAVA_HOME=/home/carsten/.hudson/tools/hudson.model.JDK/java_SDK_8u25/

### STOPPING SERVICE #####################################################
echo ""
echo "Stopping Service:"
echo "-----------------"

if [ -e "$PRODUCT_DIR/bin/$SERVICENAME" ]
then
  chmod 755 $PRODUCT_DIR/bin/$SERVICENAME
  $PRODUCT_DIR/bin/$SERVICENAME stop
else 
  echo "service not yet set up"
fi

### PREPARING SERVICE ####################################################
echo ""
echo "Preparing Service:"
echo "------------------"

cd $JOB_DIR/generated/distributions/executable

echo "copying skysail.$APPNAME.jar to $PRODUCT_DIR/bin/skysail.$APPNAME.jar"
cp skysail.$APPNAME.jar $PRODUCT_DIR/bin/skysail.$APPNAME.jar

cd $JOB_DIR
echo "copying deployment/service/* to $PRODUCT_DIR"
cp -r deployment/service/* $PRODUCT_DIR
echo "moving contents of deployment/service/shell to deployment/service/bin"
mv deployment/service/shell/* $PRODUCT_DIR/bin
echo "copying config/common/* to $PRODUCT_DIR/bin/config/common"
cp config/common/* $PRODUCT_DIR/bin/config/common
echo "copying config/$STAGE/* to $PRODUCT_DIR/bin/config/$STAGE"
cp config/$STAGE/* $PRODUCT_DIR/bin/config/$STAGE

### UPDATING CONFIG FROM SVN REP #########################################
echo ""
echo "Updating config from SVN:"
echo "-------------------------"

cd $PRODUCT_DIR/bin/config
svn export --force https://85.25.22.125/repos/skysale/skysailconfigs/$APPNAME/$STAGE/
cp $STAGE/* .

### STARTING SERVICE #####################################################
echo ""
echo "Starting Service:"
echo "-----------------"

echo "changing directory to $PRODUCT_DIR/bin/"
cd $PRODUCT_DIR/bin/
echo "unzipping skysail.$APPNAME.jar"
unzip -o skysail.$APPNAME.jar
echo "chmod 755 on $SERVICENAME"
chmod 755 $SERVICENAME
echo "running ./$SERVICENAME start"
./$SERVICENAME start

