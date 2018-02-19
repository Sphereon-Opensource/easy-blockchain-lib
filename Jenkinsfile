#!/usr/bin/groovy
@Library('pipeline-library@master')

def utils = new io.fabric8.Utils()

mavenNode {
    stage('Checkout source') {
        checkout(scm).each { k,v -> env.setProperty(k, v) }
    }

    buildLibrary{}
}
