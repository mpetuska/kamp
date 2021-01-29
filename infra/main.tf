terraform {
  backend "artifactory" {
    // -backend-config="username=xxx@xxx.com" \
    // -backend-config="password=xxxxxx" \
    username = "martynas.petuska@outlook.com"
    url      = "https://mpetuska.jfrog.io/artifactory"
    repo     = "terraform-state"
    subpath  = "kamp"
  }
}

provider "azurerm" {
  features {}
}

resource "azurerm_resource_group" "main" {
  location = "westeurope"
  name     = "kamp"
}

resource "azurerm_cosmosdb_account" "main" {
  location            = azurerm_resource_group.main.location
  name                = azurerm_resource_group.main.name
  offer_type          = "Standard"
  resource_group_name = azurerm_resource_group.main.name
  kind                = "MongoDB"
  enable_free_tier    = true
  capabilities {
    name = "AllowSelfServeUpgradeToMongo36"
  }
  capabilities {
    name = "EnableMongo"
  }
  consistency_policy {
    consistency_level = "Eventual"
  }
  geo_location {
    failover_priority = 0
    location          = azurerm_resource_group.main.location
  }
}

resource "azurerm_cosmosdb_mongo_database" "main" {
  account_name        = azurerm_cosmosdb_account.main.name
  name                = azurerm_cosmosdb_account.main.name
  resource_group_name = azurerm_cosmosdb_account.main.resource_group_name
}

resource "azurerm_cosmosdb_mongo_collection" "main" {
  account_name        = azurerm_cosmosdb_mongo_database.main.account_name
  database_name       = azurerm_cosmosdb_mongo_database.main.name
  name                = azurerm_cosmosdb_mongo_database.main.name
  resource_group_name = azurerm_cosmosdb_mongo_database.main.resource_group_name
  index {
    keys   = ["_id"]
    unique = true
  }
}

resource "azurerm_storage_account" "main" {
  name                     = azurerm_resource_group.main.name
  resource_group_name      = azurerm_resource_group.main.name
  location                 = azurerm_resource_group.main.location
  account_tier             = "Standard"
  account_replication_type = "LRS"
}

resource "azurerm_application_insights" "main" {
  name                = azurerm_resource_group.main.name
  location            = azurerm_resource_group.main.location
  resource_group_name = azurerm_resource_group.main.name
  application_type    = "web"
}

resource "azurerm_app_service_plan" "main" {
  location            = azurerm_resource_group.main.location
  name                = azurerm_resource_group.main.name
  resource_group_name = azurerm_resource_group.main.name
  sku {
    tier = "Free"
    size = "F1"
  }
}

resource "azurerm_function_app" "main" {
  name                       = azurerm_resource_group.main.name
  location                   = azurerm_app_service_plan.main.location
  resource_group_name        = azurerm_resource_group.main.name
  app_service_plan_id        = azurerm_app_service_plan.main.id
  storage_account_name       = azurerm_storage_account.main.name
  storage_account_access_key = azurerm_storage_account.main.primary_access_key
  site_config {
    use_32_bit_worker_process = true
  }
  app_settings = {
    "APPINSIGHTS_INSTRUMENTATIONKEY" = azurerm_application_insights.main.instrumentation_key
  }
}
