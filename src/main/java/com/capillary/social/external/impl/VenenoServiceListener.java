package com.capillary.social.external.impl;

import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.capillary.social.systems.config.SystemConfig;
import com.capillary.veneno.BucketDetails;
import com.capillary.veneno.CommunicationDetail;
import com.capillary.veneno.EmailBody;
import com.capillary.veneno.VenenoException;
import com.capillary.veneno.VenenoService.Iface;


public class VenenoServiceListener implements Iface {
	
	private static Logger logger = LoggerFactory.getLogger( VenenoServiceListener.class );	
	
	@Autowired
	private SystemConfig systemConfig;
	
	@Override
	public int addMessageForRecipients( CommunicationDetail thriftMessageDetails , String serverRequestID )
			throws VenenoException, TException {
				return 0;
		
	
	}
	
	@Override
	public EmailBody getMessageBody( int userId, int messageId , String serverRequestID ) throws VenenoException,
			TException {
				return null;
		
		
	}
	
	@Override
	public String getStatus( String serverRequestID ) throws VenenoException, TException {
		return null;
	}
	
	@Override
	public String replaceTemplate( int userId, int orgId, String template, String arguments , String serverRequestID )
			throws VenenoException, TException {
		return null;
	}
	
	/*private VenenoCommonService.Iface getRpcClient(){

		VenenoCommonService.Iface rpcClient;
		try {
			Service venenoService = 
            		ServiceDiscovery.getInstance().get( 
            				KnownService.VENENO_THRIFT_SERVICE );

			rpcClient = 
					RPCService.rpcClient(
							VenenoCommonService.Iface.class, 
							venenoService.getAddress(), 
							venenoService.getPort(), 
							systemConfig.VENENO_RECV_TIME_OUT );
		} catch (IOException e) {

			logger.error( " Could Not Connect To Veneno service" + e.getMessage() );
			throw new RuntimeException( "Could Not Connect with Veneno Service" );
		} catch (Exception e) {
			
			logger.error( " Could Not Connect To Veneno service" + e.getMessage() );
			throw new RuntimeException( "Could Not Connect with Veneno Service" );
		}

		return rpcClient;
	}*/

	@Override
	public boolean pauseMessage( CommunicationDetail thriftMessageDetails , String serverRequstID )
			throws VenenoException, TException {
			throw new VenenoException( " Pause is unimplemented. " );
			/*VenenoCommonService.Iface rpcClient = getRpcClient();
			
			try{
				
				return rpcClient.pauseMessage( thriftMessageDetails.getId() , serverRequstID );
			}catch( Exception e ){
				
				logger.error( " Could Not Pause" + e.getMessage() );
				throw new RuntimeException( "Could Not Connect with Veneno Service" );
			}*/
	}
	
	@Override
	public boolean isAlive() {
		
			return true;
	}

	@Override
	public BucketDetails getBucketDetailsByMessageID( int messageID , String serverRequestID )
			throws VenenoException, TException {
				return null;
		
		
	}
}
