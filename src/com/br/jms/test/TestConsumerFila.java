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
import javax.naming.InitialContext;

public class TestConsumerFila {

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
//			Session session = connection.createSession( false, Session.AUTO_ACKNOWLEDGE);
			//Alterando a confimação do recebimento da mensagem
//			Session session = connection.createSession( false, Session.CLIENT_ACKNOWLEDGE);
			//Alterando confirmação do recebimento, tornando mais dinâmico
			Session session = connection.createSession( true, Session.SESSION_TRANSACTED);
			//Pegando  fila de destino através do JNDI
			Destination fila = (Destination) context.lookup("financeiro");
			//Consumindo a fila no ApacheMQ
			MessageConsumer consumer = session.createConsumer(fila);
			//Recebendo mensagens da fila do apacha atraves do consumer
			
			//adicionando um listener no consumer
			consumer.setMessageListener(new MessageListener(){

			    @Override
			    public void onMessage(Message message){
			        TextMessage textMessage  = (TextMessage)message;
			        try{
			            System.out.println(textMessage.getText());
//			            message.acknowledge();
			            session.commit();
			        } catch(JMSException e){
			        	try {
							session.rollback();
						} catch (JMSException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
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
