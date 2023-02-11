import ext.AppExtension

plugins {
  id("convention.library-common")
}

project.extensions.create("app", AppExtension::class)
