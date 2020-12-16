output "app_service_name" {
  value = azurerm_app_service.main.name
}

output "app_service_default_hostname" {
  value = "https://${azurerm_app_service.main.default_site_hostname}"
}

output "azurerm_cosmosdb_mongo_database_connection_strings" {
  value = {
    read_write = {
      primary   = azurerm_cosmosdb_account.main.connection_strings[0]
      secondary = azurerm_cosmosdb_account.main.connection_strings[1]
    }
    read_only = {
      primary   = azurerm_cosmosdb_account.main.connection_strings[2]
      secondary = azurerm_cosmosdb_account.main.connection_strings[3]
    }
  }
  sensitive = true
}
