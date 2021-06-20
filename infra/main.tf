terraform {
  backend "artifactory" {
    // export ARTIFACTORY_USERNAME=xxx
    // export ARTIFACTORY_PASSWORD=xxx
    url     = "https://mpetuska.jfrog.io/artifactory"
    repo    = "terraform-state"
    subpath = "kamp"
  }
  required_providers {
    mongodbatlas = {
      source = "mongodb/mongodbatlas"
    }
    cloudflare = {
      source = "cloudflare/cloudflare"
    }
  }
}

provider "azurerm" {
  features {}
  // export ARM_CLIENT_ID=xxx
  // export ARM_CLIENT_SECRET=xxx
  // export ARM_SUBSCRIPTION_ID=xxx
  // export ARM_TENANT_ID=xxx
}

provider "mongodbatlas" {
  // export MONGODB_ATLAS_PUBLIC_KEY=xxx
  // export MONGODB_ATLAS_PRIVATE_KEY=xxx
}

provider "cloudflare" {
  // export CLOUDFLARE_API_TOKEN=xxx
}

resource "cloudflare_zone" "kamp" {
  zone = "kamp.ml"
}

data "cloudflare_zones" "petuska_dev" {
  filter {
    name        = "petuska.dev"
  }
}

resource "cloudflare_record" "apex" {
  zone_id = data.cloudflare_zones.petuska_dev.zones[0].id
  name    = "kamp"
  value   = "www.kamp.petuska.dev"
  type    = "CNAME"
  proxied = true
  ttl     = 1
}

resource "cloudflare_record" "www" {
  zone_id = data.cloudflare_zones.petuska_dev.zones[0].id
  name    = "www.kamp"
  value   = "gentle-mud-0876db203.azurestaticapps.net"
  type    = "CNAME"
  proxied = false
  ttl     = 1
}

resource "mongodbatlas_project" "kamp" {
  name   = "kamp"
  org_id = "60533df97f4d234d9e691ce6"
}

data "mongodbatlas_cluster" "kamp" {
  project_id = mongodbatlas_project.kamp.id
  name       = "kamp"
}

resource "mongodbatlas_database_user" "admin" {
  username           = "kamp-admin"
  password           = uuidv5("oid", "${data.mongodbatlas_cluster.kamp.id}-admin")
  project_id         = mongodbatlas_project.kamp.id
  auth_database_name = "admin"

  roles {
    role_name     = "dbAdmin"
    database_name = local.database_name
  }
  roles {
    role_name     = "readWrite"
    database_name = local.database_name
  }
}

resource "mongodbatlas_database_user" "reader" {
  username           = "kamp-reader"
  password           = uuidv5("oid", "${data.mongodbatlas_cluster.kamp.id}-reader")
  project_id         = mongodbatlas_project.kamp.id
  auth_database_name = "admin"

  roles {
    role_name     = "read"
    database_name = local.database_name
  }
}

resource "azurerm_resource_group" "kamp" {
  location = "westeurope"
  name     = "kamp"
}

resource "azurerm_application_insights" "kamp" {
  name                = azurerm_resource_group.kamp.name
  location            = azurerm_resource_group.kamp.location
  resource_group_name = azurerm_resource_group.kamp.name
  application_type    = "java"
}

resource "azurerm_app_service_plan" "kamp" {
  location            = azurerm_resource_group.kamp.location
  name                = azurerm_resource_group.kamp.name
  resource_group_name = azurerm_resource_group.kamp.name
  kind                = "Linux"
  reserved            = true
  sku {
    tier = "Free"
    size = "F1"
  }
}

locals {
  database_admin_credentials        = "${mongodbatlas_database_user.admin.username}:${mongodbatlas_database_user.admin.password}"
  database_reader_credentials       = "${mongodbatlas_database_user.reader.username}:${mongodbatlas_database_user.reader.password}"
  connection_string_chunks          = split("//", data.mongodbatlas_cluster.kamp.connection_strings[0].standard_srv)
  connection_string_options         = "ssl=true&retryWrites=true&w=majority"
  database_admin_connection_string  = "${local.connection_string_chunks[0]}//${local.database_admin_credentials}@${local.connection_string_chunks[1]}/${local.database_name}?${local.connection_string_options}"
  database_reader_connection_string = "${local.connection_string_chunks[0]}//${local.database_reader_credentials}@${local.connection_string_chunks[1]}/${local.database_name}?${local.connection_string_options}"
  database_name                     = "kamp"
}

resource "azurerm_app_service" "kamp" {
  resource_group_name = azurerm_app_service_plan.kamp.resource_group_name
  app_service_plan_id = azurerm_app_service_plan.kamp.id
  location            = azurerm_app_service_plan.kamp.location
  name                = azurerm_app_service_plan.kamp.name
  https_only          = true

  site_config {
    use_32_bit_worker_process = true
    app_command_line          = ""
    linux_fx_version          = "DOCKER|${var.docker_image_tag}"
    http2_enabled             = true
    health_check_path         = "/api/status"
    cors {
      allowed_origins = ["*"]
    }
  }

  logs {
    http_logs {
      file_system {
        retention_in_days = 35
        retention_in_mb   = 35
      }
    }
  }

  app_settings = {
    WEBSITES_ENABLE_APP_SERVICE_STORAGE = false
    DOCKER_REGISTRY_SERVER_URL          = var.docker_registry
    DOCKER_REGISTRY_SERVER_USERNAME     = var.docker_registry_username
    DOCKER_REGISTRY_SERVER_PASSWORD     = var.docker_registry_password
    MONGO_STRING                        = local.database_admin_connection_string
    MONGO_DATABASE                      = local.database_name
    ADMIN_USER                          = var.api_admin_user
    ADMIN_PASSWORD                      = var.api_admin_password
    AZURE_MONITOR_INSTRUMENTATION_KEY   = azurerm_application_insights.kamp.instrumentation_key
    APPINSIGHTS_INSTRUMENTATIONKEY      = azurerm_application_insights.kamp.instrumentation_key
    APPINSIGHTS_PROFILERFEATURE_VERSION = "1.0.0"
    JVM_ARGS                            = ""
  }
}
