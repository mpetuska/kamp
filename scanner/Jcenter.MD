
import kotlinx.serialization.Serializable

import kotlinx.serialization.SerialName
// Jcenter steps

curl 'https://bintray.com/bintray/jcenter' \
  --data-raw 'offset=160&max=8&repoPath=%2Fbintray%2Fjcenter&sortBy=lowerCaseName&filterByPkgName=' \
  --compressed > out.html // Search by class="package-name"
```html
<div class="package-name">
    <a href="/lusfold/maven/AWebDB" alt="">
        AWebDB
    </a>
</div>   
```

https://api.bintray.com/packages/mpetuska/lt.petuska/kvdom // system_ids: ["lt.petuska:kvdom"]
```json
{
  "name": "kvdom",
  "repo": "lt.petuska",
  "owner": "mpetuska",
  "desc": "",
  "labels": [
    "js",
    "kotlin",
    "mpp",
    "vdom",
    "wasm"
  ],
  "attribute_names": [
    "maturity"
  ],
  "licenses": [
    "Apache-2.0"
  ],
  "custom_licenses": [],
  "followers_count": 0,
  "created": "2020-01-30T13:07:57.952Z",
  "website_url": "",
  "issue_tracker_url": "https://gitlab.com/lt.petuska/kvdom/issues",
  "linked_to_repos": [
    "jcenter"
  ],
  "permissions": [],
  "versions": [
    "0.1.0-M2",
    "0.1.0-M1",
    "0.0.3",
    "0.0.2"
  ],
  "latest_version": "0.1.0-M2",
  "updated": "2020-07-28T08:57:00.741Z",
  "rating_count": 0,
  "system_ids": [
    "lt.petuska:kvdom-js",
    "lt.petuska:kvdom",
    "lt.petuska:kvdom-metadata",
    "lt.petuska:kvdom-wasm32"
  ],
  "vcs_url": "https://gitlab.com/lt.petuska/kvdom",
  "maturity": "Experimental"
}
```

https://dl.bintray.com/mpetuska/lt.petuska/lt/petuska/kvdom/maven-metadata.xml // <version>0.1.0-M2</version>
```xml
<metadata>
    <groupId>lt.petuska</groupId>
    <artifactId>kvdom</artifactId>
    <version>0.1.0-M2</version>
    <versioning>
        <latest>0.1.0-M2</latest>
        <release>0.1.0-M2</release>
        <versions>
            <version>0.0.2</version>
            <version>0.0.3</version>
            <version>0.1.0-M1</version>
            <version>0.1.0-M2</version>
        </versions>
        <lastUpdated>20200728085428</lastUpdated>
    </versioning>
</metadata>
```

https://dl.bintray.com/mpetuska/lt.petuska/lt/petuska/kvdom/0.1.0-M2/kvdom-0.1.0-M2.module
```json
{
  "formatVersion": "1.1",
  "component": {
    "group": "lt.petuska",
    "module": "kvdom",
    "version": "0.1.0-M2",
    "attributes": {
      "org.gradle.status": "release"
    }
  },
  "createdBy": {
    "gradle": {
      "version": "6.5.1",
      "buildId": "ulyhyoo3zzgldchabzsfi7wfu4"
    }
  },
  "variants": [
    {
      "name": "js-api",
      "attributes": {
        "org.gradle.usage": "kotlin-api",
        "org.jetbrains.kotlin.js.compiler": "legacy",
        "org.jetbrains.kotlin.platform.type": "js"
      },
      "available-at": {
        "url": "../../kvdom-js/0.1.0-M2/kvdom-js-0.1.0-M2.module",
        "group": "lt.petuska",
        "module": "kvdom-js",
        "version": "0.1.0-M2"
      }
    },
    {
      "name": "js-runtime",
      "attributes": {
        "org.gradle.usage": "kotlin-runtime",
        "org.jetbrains.kotlin.js.compiler": "legacy",
        "org.jetbrains.kotlin.platform.type": "js"
      },
      "available-at": {
        "url": "../../kvdom-js/0.1.0-M2/kvdom-js-0.1.0-M2.module",
        "group": "lt.petuska",
        "module": "kvdom-js",
        "version": "0.1.0-M2"
      }
    },
    {
      "name": "metadata-api",
      "attributes": {
        "org.gradle.usage": "kotlin-metadata",
        "org.jetbrains.kotlin.platform.type": "common"
      },
      "available-at": {
        "url": "../../kvdom-metadata/0.1.0-M2/kvdom-metadata-0.1.0-M2.module",
        "group": "lt.petuska",
        "module": "kvdom-metadata",
        "version": "0.1.0-M2"
      }
    },
    {
      "name": "wasm32-api",
      "attributes": {
        "artifactType": "org.jetbrains.kotlin.klib",
        "org.gradle.usage": "kotlin-api",
        "org.jetbrains.kotlin.native.target": "wasm32",
        "org.jetbrains.kotlin.platform.type": "native"
      },
      "available-at": {
        "url": "../../kvdom-wasm32/0.1.0-M2/kvdom-wasm32-0.1.0-M2.module",
        "group": "lt.petuska",
        "module": "kvdom-wasm32",
        "version": "0.1.0-M2"
      }
    }
  ]
}
```
