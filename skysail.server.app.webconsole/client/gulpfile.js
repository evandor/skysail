'use strict';

var changed = require('gulp-changed');
var childProcess = require('child_process');
var del = require('del');
var gulp = require('gulp');
var runSequence = require('run-sequence');
var sourcemaps = require('gulp-sourcemaps');
var concat = require('gulp-concat');
var ts = require('gulp-typescript');
var minimist = require('minimist');

var packageJson = require('./package.json');

var spawn = childProcess.spawn;
var server;

var PATHS = {
  libs: [
    'node_modules/angular2/bundles/angular2.dev.js',
    'node_modules/angular2/bundles/http.dev.js',
    'node_modules/angular2/bundles/router.dev.js',
    'node_modules/systemjs/dist/system.src.js'
  ],
  client: {
    ts: ['client/**/*.ts'],
    html: 'client/**/*.html',
    css: 'client/**/*.css',
    img: 'client/**/*.{svg,jpg,png,ico}'
  },
  dist: 'dist',
  distClient: 'dist/client',
  distLib: 'dist/lib',
  port: 8080
};

var tsProject = ts.createProject('tsconfig.json');

gulp.task('clean', function(done) {
  del([PATHS.dist], done);
});

gulp.task('ts', function() {
  return gulp
    .src(PATHS.client.ts)
    .pipe(changed(PATHS.distClient, {
      extension: '.js'
    }))
    .pipe(sourcemaps.init())
    .pipe(ts(tsProject))
    .pipe(sourcemaps.write('.'))
    .pipe(gulp.dest(PATHS.distClient));
});

gulp.task('html', function() {
  return gulp
    .src(PATHS.client.html)
    .pipe(changed(PATHS.distClient))
    .pipe(gulp.dest(PATHS.distClient));
});

gulp.task('css', function() {
  return gulp
    .src(PATHS.client.css)
    .pipe(changed(PATHS.distClient, {
      extension: '.css'
    }))
    .pipe(sourcemaps.init())
    .pipe(concat('app.css'))
    .pipe(sourcemaps.write('.'))
    .pipe(gulp.dest(PATHS.distClient));
});

gulp.task('img', function() {
  return gulp
    .src(PATHS.client.img)
    .pipe(changed(PATHS.distClient))
    .pipe(gulp.dest(PATHS.distClient));
});

gulp.task('bundle', function(done) {
  runSequence('clean', ['libs', 'ts', 'html', 'css', 'img'], done);
});

gulp.task('server:restart', function(done) {
  var started = false;
  if (server) {
    server.kill();
  }
  var args = minimist(process.argv.slice(2), {default: {port: '8080'}});
  server = spawn('node', [packageJson.main, '--port', args.port]);
  server.stdout.on('data', function(data) {
    console.log(data.toString());
    if (started === false) {
      started = true;
      done();
    }
  });
  server.stderr.on('data', function(data) {
    console.error(data.toString());
  });
});

// clean up if an error goes unhandled.
process.on('exit', function() {
  if (server) {
    server.kill();
  }
});

gulp.task('libs', function() {
  return gulp
    .src(PATHS.libs)
    .pipe(changed(PATHS.distLib))
    .pipe(gulp.dest(PATHS.distLib));
})

gulp.task('go', ['bundle', 'server:restart'], function() {
  gulp.watch(PATHS.client.ts, ['ts']);
  gulp.watch(PATHS.client.html, ['html']);
  gulp.watch(PATHS.client.css, ['css']);
  gulp.watch(PATHS.client.img, ['img']);
  gulp.watch(packageJson.main, ['server:restart']);
});

gulp.task('default', ['bundle']);
