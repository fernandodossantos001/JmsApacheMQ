package com.br.jms.test;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.naming.InitialContext;

import br.com.caelum.modelo.Pedido;
import br.com.caelum.modelo.PedidoFactory;

public class TestProducerTopico {

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

			Pedido pedido = new PedidoFactory().geraPedidoComValores();
//			StringWriter writer = new StringWriter();
//			JAXB.marshal(pedido, writer);
//			String xml = writer.toString();
			
//			System.out.println(xml);
			
			
//				TextMessage message = session.createTextMessage("<pedido><id>XPTO</id></pedido>");
//				TextMessage message = session.createTextMessage(xml);
				Message message = session.createObjectMessage(pedido);
				message.setBooleanProperty("ebook", false);
				producer.send(message);
			
//			Message receive = consumer.receive();
			
//			System.out.println(receive);
			
			
			session.close();
			context.close();
			connection.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
