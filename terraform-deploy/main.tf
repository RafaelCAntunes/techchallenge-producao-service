terraform {
  required_providers {
    kubernetes = {
      source  = "hashicorp/kubernetes"
      version = ">= 2.30.0"
    }
    aws = {
      source  = "hashicorp/aws"
      version = "~> 4.0"
    }
  }
}

terraform {
  backend "remote" {
    organization = "techchallenge-lanchonete"

    workspaces {
      name = "techchallenge-producao-service"
    }
  }
}

provider "aws" {
  region = var.aws_region
}


data "terraform_remote_state" "eks" {
  backend = "remote"

  config = {
    organization = "techchallenge-lanchonete"
    workspaces = {
      name = "techchallenge-infra-k8s"
    }
  }
}

data "terraform_remote_state" "db" {
  backend = "remote"

  config = {
    organization = "techchallenge-lanchonete"
    workspaces = {
      name = "techchallenge-infra-db"
    }
  }
}

data "aws_eks_cluster" "cluster" {
  name = data.terraform_remote_state.eks.outputs.cluster_name
}

data "aws_eks_cluster_auth" "cluster" {
  name = data.terraform_remote_state.eks.outputs.cluster_name
}

provider "kubernetes" {
  host                   = data.aws_eks_cluster.cluster.endpoint
  cluster_ca_certificate = base64decode(data.aws_eks_cluster.cluster.certificate_authority[0].data)
  token                  = data.aws_eks_cluster_auth.cluster.token
}
