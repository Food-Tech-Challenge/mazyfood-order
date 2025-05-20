# language: pt

  Funcionalidade: Pedido de combo de produtos

  Como cliente da MazyFood
  Quero poder montar um combo com hambúrguer, acompanhamento e bebida
  Para realizar um pedido completo

    Cenário: Iniciar pedido e adicionar produto
      Quando eu iniciar um pedido
      E eu adiciono o produto 10 com nome "Pizza", preço 25.00 e quantidade 2 ao pedido

    Cenário: Pagar meu pedido
      Dado que existe um pedido com id 1
      Quando realizo o pagamento



