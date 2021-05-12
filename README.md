<h1 align="center">
  <br>
  <a href="https://www.sphereon.com"><img src="https://sphereon.com/content/themes/sphereon/assets/img/logo.svg" alt="Sphereon" width="400"></a>
  <br> Java implementation easy blockchain API
  <br> with useful functions for the  
  <br> Easy Blockchain API
  <br>
</h1>

You can use this Java library to perform common functions for the [Easy Blockchain API](https://docs.sphereon.com/api/easy-blockchain/0.10/html)

## Key Features
* Determine Chain and Entry IDs before anchoring in the blockchain, see: [id calculation documentation](https://docs.sphereon.com/api/easy-blockchain/0.10/html#_id_calculation)
* Create hashes of content
* Create chain links to other chains, entries or contexts at any level, see: [chainlink documentation](https://docs.sphereon.com/api/easy-blockchain/0.10/html#_chain_links)

## How To Use
To clone and use this library application, you'll need [Git](https://git-scm.com), [Maven](https://maven.apache.org/) and your favorite Java IDE.
Of course we also provide releases in our maven repository 

```bash
# Clone this repository
> git clone https://github.com/Sphereon-OpenSource/easy-blockchain-lib.git

# Go into the repository
> cd easy-blockchain-lib

# Build and install into your Maven repository
> mvn clean install

# Use the artifact in your Maven pom or Gralde build configuration  
    <repositories>
        <repository>
            <id>sphereon-public</id>
            <name>Sphereon Public</name>
            <url>https://nexus.qa.sphereon.com/repository/sphereon-public/</url>
         </repository>
    </repositories>
...
    <dependency>
        <groupId>com.sphereon.public</groupId>
        <artifactId>easy-blockchain-lib-main</artifactId> <!-- Use easy-blockchain-lib-osgi for OSGI bundle  -->
        <version>0.1.0</version>
    </dependency>
```


#### License
[Apache2](https://www.apache.org/licenses/LICENSE-2.0)

---
Links
> [Documentation](https://docs.sphereon.com/api/easy-blockchain/0.10/html) &nbsp;&middot;&nbsp;
> [SDKs](https://github.com/Sphereon-SDK/easy-blockchain-sdk) &nbsp;&middot;&nbsp;
> [Java SDK](https://github.com/Sphereon-SDK/easy-blockchain-sdk/tree/develop/java8-okhttp-gson) &nbsp;&middot;&nbsp;
> [Sphereon.com](https://www.sphereon.com) &nbsp;&middot;&nbsp;
