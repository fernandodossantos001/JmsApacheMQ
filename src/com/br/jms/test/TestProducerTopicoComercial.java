package com.br.jms.test;

import java.util.Scanner;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;

public class TestProducerTopicoComercial {

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		try {
			//Pegando configuracoes do connection factory do JNDI properties
			InitialContext context = new InitialContext();//  iniciando o contexto, através dele será possível pegar os parametros do JNDI
			ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");//Pegando o nome da conexao com o ActiveMQ
			//Preparando conexao com o activeMQO
			Connection connection =   factory.createConnection();
			//abrindo conexao
			connection.start();
			//Recebendo uma Session para receber as mensagens
			// primeiro parametro indica se faremos alguma transacao, o segundo indica a confirmacao automatica do recebimento da mensagem
			Session session = connection.createSession( false, Session.AUTO_ACKNOWLEDGE);
			//Pegando  fila de destino através do JNDI
			Destination topico = (Destination) context.lookup("loja");
			//Produzindo para a fila no ApacheMQ
			MessageProducer producer = session.createProducer(topico);
			//Recebendo mensagens da fila do apacha atraves do consumer
			
			
				TextMessage message = session.createTextMessage("<pedido><id></id></pedido>");
				producer.send(message);
			
//			Message receive = consumer.receive();
			
//			System.out.println(receive);
			
			new Scanner(System.in).hasNextLine();
			
			session.close();
			context.close();
			connection.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
