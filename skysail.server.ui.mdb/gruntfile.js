'use strict';

module.exports = function (grunt) {
    grunt.loadNpmTasks('grunt-sass');

    grunt.initConfig({
        sass: {
            options: {},
            dist: {
                files: [
                    {
                        expand: true,     // Enable dynamic expansion.
                        cwd: 'src/main/resources',      // Src matches are relative to this path.
                        src: ['**/*.scss'], // Actual pattern(s) to match.
                        dest: 'build/resources/main',   // Destination path prefix.
                        ext: '.css',   // Dest filepaths will have this extension.
                        extDot: 'first'   // Extensions in filenames begin after the first dot
                    }
                ]
            }
        }
    });
    grunt.registerTask('build', ['sass']);
};