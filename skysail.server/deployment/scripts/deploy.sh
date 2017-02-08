JOB_DIR="/home/carsten/.hudson/jobs/ssp.$APPNAME.export.$STAGE/workspace/skysail.product.$APPNAME"
PRODUCT_DIR="/home/carsten/skysail/products/$APPNAME/$STAGE"
SERVICENAME=${APPNAME}_${STAGE}
export JAVA_HOME=/home/carsten/.hudson/tools/hudson.model.JDK/java_SDK_8u25/

echo "APPNAME:     $APPNAME"
echo "STAGE:       $STAGE"
echo "JOB_DIR:     $JOB_DIR"
echo "PRODUCT_DIR: $PRODUCT_DIR"
echo "SERVICENAME: $SERVICENAME"

### DIRECTORY MANAGEMENT #################################################
echo ""
echo "Setting up directory structures:"
echo "--------------------------------"

echo "clearing configuration dir $PRODUCT_DIR/bin/config"
rm -rf $PRODUCT_DIR/bin/config
mkdir -p $PRODUCT_DIR/bin/config/$STAGE
mkdir -p $PRODUCT_DIR/bin/config/common

echo "clearing jars in dir $PRODUCT_DIR/bin/jar"
rm -rf $PRODUCT_DIR/bin/jar
mkdir -p $PRODUCT_DIR/bin/jar

echo "creating dir $PRODUCT_DIR/lib if not existent"
mkdir -p $PRODUCT_DIR/lib

echo "moving old wrapper logfile"
if [ -e "$PRODUCT_DIR/bin/wrapper.log" ]
then
  mv $PRODUCT_DIR/bin/wrapper.log $PRODUCT_DIR/bin/wrapper.lastRun.log 
fi

### ZIP ARCHIVE ##########################################################
echo ""
echo "Creating ZIP Archive:"
echo "--------------------"

cd $JOB_DIR/generated/distributions/executable
cp $APPNAME.$STAGE.jar skysail.$APPNAME.jar

zip -r skysail.$APPNAME.zip ../../../config/$STAGE skysail.$APPNAME.jar

### PUBLIC SITE ##########################################################
echo ""
echo "Manage public site:"
echo "-------------------"

echo "copying skysail.$APPNAME.zip to public site"
mkdir -p /var/www/skysail.io/public_html/skysail/products/$APPNAME
cp skysail.$APPNAME.zip /var/www/downloads.skysail.io/skysail/products/$APPNAME/skysail.$APPNAME.$STAGE.zip
cp skysail.$APPNAME.jar /var/www/downloads.skysail.io/skysail/products/$APPNAME/skysail.$APPNAME.$STAGE.jar

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

