resource "kubernetes_service" "producao" {
  metadata {
    name = "producao-service"
  }

  spec {
    selector = {
      app = "producao"
    }

    port {
      port        = 80
      target_port = 8080
    }

    type = "LoadBalancer"
  }
}