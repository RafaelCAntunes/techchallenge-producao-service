resource "kubernetes_deployment" "producao" {
  metadata {
    name = "producao"
    labels = {
      app = "producao"
    }
  }

  spec {
    replicas = 2

    selector {
      match_labels = {
        app = "producao"
      }
    }

    template {
      metadata {
        labels = {
          app = "producao"
        }
      }

      spec {
        container {
          name  = "producao"
          image = "165835313479.dkr.ecr.us-east-1.amazonaws.com/techchallenge_lanchonete-producao:latest"

          port {
            container_port = 8080
          }

          # DynamoDB
          env {
            name  = "AWS_REGION"
            value = var.aws_region
          }

          env {
            name  = "DYNAMODB_PRODUCAO_TABLE"
            value = data.terraform_remote_state.db.outputs.dynamodb_producao_table_name
          }

          # URLs do pedido
          env {
            name  = "PEDIDOS_SERVICE_URL"
            value = data.terraform_remote_state.pedido.outputs.service_hostname
          }

          env {
            name  = "SPRING_PROFILES_ACTIVE"
            value = "prod"
          }

          resources {
            limits = {
              cpu    = "400m"
              memory = "512Mi"
            }
            requests = {
              cpu    = "200m"
              memory = "256Mi"
            }
          }

          liveness_probe {
            http_get {
              path = "/actuator/health/liveness"
              port = 8080
            }
            initial_delay_seconds = 60
            period_seconds        = 30
            failure_threshold     = 3
          }

          readiness_probe {
            http_get {
              path = "/actuator/health/readiness"
              port = 8080
            }
            initial_delay_seconds = 40
            period_seconds        = 10
            failure_threshold     = 3
          }
        }
      }
    }
  }
}