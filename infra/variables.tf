variable "docker_registry" {
  type    = string
  default = "https://docker.pkg.github.com"
}

variable "docker_registry_username" {
  type    = string
  default = "mpetuska"
}

variable "docker_registry_password" {
  type      = string
  sensitive = true
}

variable "docker_image_tag" {
  type = string
}

variable "api_admin_user" {
  type = string
}

variable "api_admin_password" {
  type = string
}
