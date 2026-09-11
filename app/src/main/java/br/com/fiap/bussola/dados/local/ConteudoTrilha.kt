package br.com.fiap.bussola.dados.local

import br.com.fiap.bussola.dominio.modelo.Alternativa
import br.com.fiap.bussola.dominio.modelo.Modulo
import br.com.fiap.bussola.dominio.modelo.Questao

object ConteudoTrilha {

    val modulos: List<Modulo> = listOf(
        Modulo(
            id = "orcamento",
            ordem = 1,
            numeroRomano = "I",
            titulo = "Onde vai meu dinheiro",
            duracaoMinutos = 4,
            paragrafos = listOf(
                "Quase ninguem gasta mal por falta de disciplina. Gasta mal porque nao " +
                    "enxerga o proprio mes inteiro de uma vez.",
                "Anotar entradas e saidas por trinta dias costuma revelar entre 10% e 20% " +
                    "de gasto que a pessoa jurava nao ter. Nao e desperdicio moral: e o " +
                    "acumulo de pequenas decisoes que nunca foram somadas.",
                "O objetivo do registro nao e cortar prazeres. E saber quanto sobra de " +
                    "verdade, para poder decidir sobre esse valor."
            ),
            destaque = "Voce nao controla o que nao mede.",
            questoes = listOf(
                Questao(
                    enunciado = "Qual e o primeiro passo para organizar o orcamento?",
                    alternativas = listOf(
                        Alternativa("Cortar todos os gastos de lazer", false),
                        Alternativa("Registrar tudo o que entra e o que sai", true),
                        Alternativa("Pedir um emprestimo para quitar o mes", false)
                    ),
                    explicacao = "Sem registro nao ha diagnostico, e sem diagnostico " +
                        "qualquer corte e chute."
                ),
                Questao(
                    enunciado = "O saldo do mes e:",
                    alternativas = listOf(
                        Alternativa("O que entrou menos o que saiu", true),
                        Alternativa("O limite disponivel no cartao", false),
                        Alternativa("O valor do salario", false)
                    ),
                    explicacao = "Limite de cartao e divida disponivel, nunca dinheiro seu."
                )
            )
        ),
        Modulo(
            id = "juros",
            ordem = 2,
            numeroRomano = "II",
            titulo = "Juros compostos",
            duracaoMinutos = 5,
            paragrafos = listOf(
                "Juro simples cresce em linha reta. Juro composto cresce sobre o proprio " +
                    "crescimento, e por isso a conta escapa da intuicao.",
                "Uma divida a 15% ao mes nao dobra em pouco mais de seis meses: ela " +
                    "multiplica por mais de cinco em doze meses. O mesmo mecanismo que " +
                    "trabalha contra voce em uma divida trabalha a seu favor em uma reserva.",
                "Por isso o tempo e a variavel mais poderosa da conta. Antecipar uma " +
                    "negociacao em tres meses muda o resultado mais do que qualquer corte " +
                    "de gasto no mesmo periodo."
            ),
            destaque = "Juro composto nao e intuicao, e conta. Faca a conta antes de decidir.",
            questoes = listOf(
                Questao(
                    enunciado = "Em juros compostos, o juro do segundo mes incide sobre:",
                    alternativas = listOf(
                        Alternativa("Apenas o valor original da divida", false),
                        Alternativa("O valor original mais o juro do primeiro mes", true),
                        Alternativa("Somente o juro do primeiro mes", false)
                    ),
                    explicacao = "E exatamente esse efeito em cascata que faz a divida " +
                        "acelerar mes a mes."
                ),
                Questao(
                    enunciado = "Uma divida a 15% ao mes, em doze meses, aproximadamente:",
                    alternativas = listOf(
                        Alternativa("Dobra de tamanho", false),
                        Alternativa("Multiplica por mais de cinco", true),
                        Alternativa("Cresce 15% no total", false)
                    ),
                    explicacao = "15% ao mes equivale a cerca de 435% ao ano, nao a 180%."
                )
            )
        ),
        Modulo(
            id = "rotativo",
            ordem = 3,
            numeroRomano = "III",
            titulo = "Sair do rotativo",
            duracaoMinutos = 6,
            paragrafos = listOf(
                "O rotativo e o credito acionado quando voce paga menos que a fatura " +
                    "inteira. E a linha mais cara do mercado brasileiro.",
                "Desde 2017 o banco e obrigado a oferecer o parcelamento do saldo devedor " +
                    "depois de trinta dias no rotativo, com taxa menor. Essa troca nao e um " +
                    "favor: e um direito do cliente, e precisa ser pedida.",
                "A frase que resolve a maior parte dos casos e curta: quero parcelar o " +
                    "saldo devedor, qual e a taxa? Anote a resposta e compare com outras " +
                    "opcoes antes de aceitar."
            ),
            destaque = "A portabilidade para um parcelamento comum costuma cortar o juro pela metade.",
            questoes = listOf(
                Questao(
                    enunciado = "Qual atitude reduz mais o custo da divida?",
                    alternativas = listOf(
                        Alternativa("Pagar so o minimo todo mes", false),
                        Alternativa("Negociar o parcelamento do saldo devedor", true),
                        Alternativa("Abrir outro cartao para pagar o primeiro", false)
                    ),
                    explicacao = "Pagar o minimo mantem o saldo na linha mais cara. Abrir " +
                        "outro cartao apenas troca de credor e soma anuidades."
                ),
                Questao(
                    enunciado = "Pedir o parcelamento do saldo devedor e:",
                    alternativas = listOf(
                        Alternativa("Um favor que o banco pode negar sem motivo", false),
                        Alternativa("Um direito previsto na regulacao desde 2017", true),
                        Alternativa("Um servico exclusivo de conta premium", false)
                    ),
                    explicacao = "A oferta e obrigatoria apos trinta dias de rotativo."
                ),
                Questao(
                    enunciado = "Antes de aceitar a primeira proposta, o certo e:",
                    alternativas = listOf(
                        Alternativa("Aceitar rapido para nao perder a oferta", false),
                        Alternativa("Anotar a taxa e comparar com outras opcoes", true),
                        Alternativa("Ignorar a taxa e olhar so o valor da parcela", false)
                    ),
                    explicacao = "Parcela pequena com prazo longo pode custar mais no total."
                )
            )
        ),
        Modulo(
            id = "reserva",
            ordem = 4,
            numeroRomano = "IV",
            titulo = "Reserva de emergencia",
            duracaoMinutos = 5,
            paragrafos = listOf(
                "Divida cara quase nunca comeca com consumo. Comeca com imprevisto: " +
                    "remedio, conserto, viagem urgente.",
                "Reserva de emergencia e o dinheiro que impede o imprevisto de virar " +
                    "divida. Nao precisa ser grande para funcionar: precisa existir antes " +
                    "do problema.",
                "Guardar um valor fixo todo mes, mesmo pequeno, vale mais do que esperar " +
                    "sobrar. O que sobra depois de gastar quase nunca sobra."
            ),
            destaque = "Reserva nao e investimento. E o que evita o juro mais caro do mercado.",
            questoes = listOf(
                Questao(
                    enunciado = "A funcao principal da reserva de emergencia e:",
                    alternativas = listOf(
                        Alternativa("Render mais que a poupanca", false),
                        Alternativa("Evitar que um imprevisto vire divida cara", true),
                        Alternativa("Financiar uma compra planejada", false)
                    ),
                    explicacao = "O ganho da reserva e o juro que voce deixa de pagar."
                ),
                Questao(
                    enunciado = "A melhor forma de construir a reserva e:",
                    alternativas = listOf(
                        Alternativa("Guardar o que sobrar no fim do mes", false),
                        Alternativa("Separar um valor fixo assim que o dinheiro entra", true),
                        Alternativa("Esperar um aumento de salario", false)
                    ),
                    explicacao = "Separar primeiro transforma a reserva em despesa fixa, " +
                        "e despesa fixa a gente paga."
                )
            )
        ),
        Modulo(
            id = "golpes",
            ordem = 5,
            numeroRomano = "V",
            titulo = "Golpes financeiros",
            duracaoMinutos = 4,
            paragrafos = listOf(
                "Golpe financeiro raramente comeca com um pedido de dinheiro. Comeca com " +
                    "pressa, com sigilo ou com uma oferta boa demais.",
                "Banco nao liga pedindo senha, nao pede transferencia para conta de " +
                    "seguranca e nao envia link para atualizar cadastro. Se a mensagem cria " +
                    "urgencia, esse e o sinal.",
                "Antes de agir, encerre o contato e ligue para o numero impresso no verso " +
                    "do cartao. Dez minutos de conferencia custam menos que um mes de salario."
            ),
            destaque = "Pressa e a ferramenta principal de quem aplica golpe.",
            questoes = listOf(
                Questao(
                    enunciado = "Uma ligacao pede sua senha para cancelar uma compra suspeita. Voce deve:",
                    alternativas = listOf(
                        Alternativa("Informar a senha para agilizar o cancelamento", false),
                        Alternativa("Encerrar e ligar para o numero do verso do cartao", true),
                        Alternativa("Enviar a senha por mensagem, que e mais seguro", false)
                    ),
                    explicacao = "Nenhuma instituicao legitima pede senha por telefone ou mensagem."
                ),
                Questao(
                    enunciado = "O sinal mais comum de golpe e:",
                    alternativas = listOf(
                        Alternativa("A urgencia criada pelo interlocutor", true),
                        Alternativa("O horario comercial da ligacao", false),
                        Alternativa("O uso do seu primeiro nome", false)
                    ),
                    explicacao = "A pressa existe para impedir que voce confira a informacao."
                )
            )
        )
    )
}
