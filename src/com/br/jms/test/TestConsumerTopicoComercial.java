package com.br.jms.test;

import java.util.Scanner;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.InitialContext;

public class TestConsumerTopicoComercial {

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		try {
			//Pegando configuracoes do connection factory do JNDI properties
			InitialContext context = new InitialContext();//  iniciando o contexto, através dele será possível pegar os parametros do JNDI
			ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");//Pegando o nome da conexao com o ActiveMQ
			//Preparando conexao com o activeMQO
			Connection connection =   factory.createConnection();
			//abrindo conexao
			//devemos identificar uma conexão com o método
			connection.setClientID("comercial");
			connection.start();
			//Recebendo uma Session para receber as mensagens
			// primeiro parametro indica se faremos alguma transacao, o segundo indica a confirmacao automatica do recebimento da mensagem
			Session session = connection.createSession( false, Session.AUTO_ACKNOWLEDGE);
			//Pegando  fila de destino através do JNDI
			Topic topico = (Topic) context.lookup("loja");
			//Consumindo a fila no ApacheMQ
//			MessageConsumer consumer = session.createConsumer(topico);
			//e identificar a assinatura (o consumidor) com o método
			MessageConsumer consumer = session.createDurableSubscriber(topico, "assinatura");
			//Recebendo mensagens da fila do apacha atraves do consumer
			
			//adicionando um listener no consumer
			consumer.setMessageListener(new MessageListener(){

			    @Override
			    public void onMessage(Message message){
			        TextMessage textMessage  = (TextMessage)message;
			        try{
			            System.out.println(textMessage.getText());
			        } catch(JMSException e){
			            e.printStackTrace();
			        }    
			    }

			});
			
			
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
