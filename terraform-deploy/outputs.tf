output "service_hostname" {
  value = try(
    kubernetes_service.producao.status[0].load_balancer[0].ingress[0].hostname,
    "pending"
  )
  description = "O hostname público do LoadBalancer. Pode demorar alguns minutos para aparecer após o apply."
}


output "deployment_info" {
    producao = {
      name     = kubernetes_deployment.producao.metadata[0].name
      replicas = kubernetes_deployment.producao.spec[0].replicas
    }
  description = "Informações do deployment"
}