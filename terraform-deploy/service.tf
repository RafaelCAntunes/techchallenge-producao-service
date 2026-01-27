resource "kubernetes_service" "producao" {
  metadata {
    name = "pagamento-service"
  }

  spec {
    selector = {
      app = "pagamento"
    }

    port {
      port        = 80
      target_port = 8080
    }

    type = "LoadBalancer"
  }
}