# Deployment Descriptor Metadata for WildFly

This library is used for parsing Jakarta EE and WildFly-specific deployment descriptor files to them and creating a Java object model representation of their content.


# Building

Prerequisites:

* JDK 11 or newer - check `java -version`
* Maven 3.6.0 or newer - check `mvn -v`
* On *nix systems, make sure that the maximum number of open files for the user running the build is at least 4096
  (check `ulimit -n`) or more, depending on what other i/o intensive processes the user is running.

To build with your own Maven installation:

    mvn install

# Releasing 

To release JBoss Metadata first checkout the project and ensure you are on the latest commit for the `main` branch with no local changes.

Prior to releasing you should ensure you have your own GPG signing key set up, published to a key server and listed on [wildfly.org](https://www.wildfly.org/contributors/pgp/).

## Prepare the release

Execute:

    mvn release:prepare -Pjboss-release

Enter the version being released:

    What is the release version for "JBoss Metadata"? (metadata) 17.0.0: 17.0.0.Final

Don't forget to put the qualifier (e.g. Final) in the release tag name.

The tag will default to the version:

    What is the SCM release tag or label for "JBoss Metadata"? (metadata) 17.0.0.Final:

Set the next version:

    What is the new development version for "JBoss Metadta"? (metadata) 17.0.0.Alpha1-SNAPSHOT: 17.0.1-SNAPSHOT

The release commit can be checked with:

    git show ${TAG}

If everything is Ok perform the release which will deploy to Nexus.

## Perform the release

Execute:

    mvn release:perform -Pjboss-release

This will deploy the release to the `wildfly-staging` repository.

Wait for 10 minutes then visit the Validation task for the `wildfly-staging` repository in Nexus. If this task ran at least 10 minutes after the release was deployed check the latest results on the Settings tab and verify that at least one component was processed and that there were no errors. If the task has not run it can be manually kicked off using the Run button.

e.g.

> Processed 9 components.
> - no errors were found.
> - the deployment was a dry run (no actual publishing).

If others are also deploying at the same time this count could be higher, the important check is that the scan was at least 10 minutes after it was deployed, 9 or more components were scanned and no errors specific to JBoss Metadata are reported.

## Complete the release

If no issues are reported complete the release.

Move the component to the `releases` repository:

    git checkout ${TAG}
    mvn nxrm3:staging-move
    git checkout main

Push the branch and tag to GitHub:

    git push upstream main
    git push upstream ${TAG}

## Rollback the Release

If the release failed, revert the release.

Delete the component from Nexus:

    git checkout ${TAG}
    mvn nxrm3:staging-delete
    git checkout main

Reset your local Git checkout:

    git reset --hard upstream/main
    git tag --delete ${TAG}


# License

* [Apache License Version 2.0](https://www.apache.org/licenses/LICENSE-2.0)