resource "kubernetes_horizontal_pod_autoscaler_v2" "app" {
  metadata {
    name      = "producao-hpa"
  }

  spec {
    scale_target_ref {
      api_version = "apps/v1"
      kind        = "Deployment"
      name        = kubernetes_deployment.producao.metadata[0].name
    }

    min_replicas = 1
    max_replicas = 2

    metric {
      type = "Resource"

      resource {
        name = "cpu"

        target {
          type               = "Utilization"
          average_utilization = 50
        }
      }
    }
  }
}