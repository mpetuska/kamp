output "app_service_name" {
  value = azurerm_app_service.kamp.name
}

output "app_service_default_hostname" {
  value = "https://${azurerm_app_service.kamp.default_site_hostname}"
}

output "mongodbatlas_kamp_reader_credentials" {
  value = local.database_reader_credentials
}

output "mongodbatlas_kamp_reader_connection_string" {
  value = local.database_reader_connection_string
}

output "mongodbatlas_kamp_admin_credentials" {
  value     = local.database_admin_credentials
  sensitive = true
}

output "mongodbatlas_kamp_admin_connection_string" {
  value     = local.database_admin_connection_string
  sensitive = true
}
