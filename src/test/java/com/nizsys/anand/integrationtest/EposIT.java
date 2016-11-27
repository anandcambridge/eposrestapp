package com.nizsys.anand.integrationtest;

import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.junit.Test;

public class EposIT {

	
	/***********************************************************/
	@Test
	public void givenValidURI_CheckStatus_OK()
	      throws ClientProtocolException, IOException {
	   // Given
	   HttpUriRequest request = new HttpGet( "http://localhost:8080/epos/products?productlist=apple&productlist=Orange");
	 
	   // When
	   HttpResponse httpResponse = HttpClientBuilder.create().build().execute( request );

	   // Then
	   Assert.assertThat(httpResponse.getStatusLine().getStatusCode(), equalTo(HttpStatus.SC_OK));
	}
	
	@Test
	public void givenURIWithProudct_ExpectNonZeroInResult_HappyPath()
	      throws ClientProtocolException, IOException {
	   // Given
	   HttpUriRequest request = new HttpGet( "http://localhost:8080/epos/products?productlist=apple&productlist=Orange");
	 
	   // When
	   HttpResponse httpResponse = HttpClientBuilder.create().build().execute( request );

	   // Then
	   String jsonFromResponse = EntityUtils.toString(httpResponse.getEntity());
	   Assert.assertThat("£0.85", equalTo(jsonFromResponse));
	}
	
	@Test
	public void givenURIWithNoProudct_ExpectZeroInResult()
	      throws ClientProtocolException, IOException {
	   // Given
	   HttpUriRequest request = new HttpGet( "http://localhost:8080/epos/products");
	 
	   // When
	   HttpResponse httpResponse = HttpClientBuilder.create().build().execute( request );

	   // Then
	   Assert.assertThat(httpResponse.getStatusLine().getStatusCode(), equalTo(HttpStatus.SC_OK));
	   
	   String jsonFromResponse = EntityUtils.toString(httpResponse.getEntity());
	   Assert.assertThat("£0.00", equalTo(jsonFromResponse));
	}

	@Test
	public void givenURIWithInvalidProudct_ExpectErrorInResult()
	      throws ClientProtocolException, IOException {
	   // Given
	   HttpUriRequest request = new HttpGet( "http://localhost:8080/epos/products?productlist=apple&productlist=aaa");
	 
	   // When
	   HttpResponse httpResponse = HttpClientBuilder.create().build().execute( request );

	   // Then
	   Assert.assertThat(httpResponse.getStatusLine().getStatusCode(), equalTo(HttpStatus.SC_OK));
	   
	   String jsonFromResponse = EntityUtils.toString(httpResponse.getEntity());
	   Assert.assertThat("[ERR-100] Product 'aaa' entered is invalid.", equalTo(jsonFromResponse));
	}
	/***********************************************************/
	
	@Test
	public void givenURIWithProudct_WithOffer_ExpectNonZeroInResult_HappyPath()
	      throws ClientProtocolException, IOException {
	   // Given
	   HttpUriRequest request = new HttpGet( "http://localhost:8080/epos/products?productlist=apple,apple,apple,Orange,Orange,Orange,Orange&offerlist=OFFER_BOGOF-apple,offer_3for2-Orange");
	 
	   // When
	   HttpResponse httpResponse = HttpClientBuilder.create().build().execute( request );

	   // Then
	   String jsonFromResponse = EntityUtils.toString(httpResponse.getEntity());
	   Assert.assertThat("£1.95", equalTo(jsonFromResponse));
	}
	
	@Test
	public void givenURIWithProudct_InvalidOffer_ExpectError()
	      throws ClientProtocolException, IOException {
	   // Given
	   HttpUriRequest request = new HttpGet( "http://localhost:8080/epos/products?productlist=orange,apple&offerlist=OFFER_BOGOF-orange");
	 
	   // When
	   HttpResponse httpResponse = HttpClientBuilder.create().build().execute( request );

	   // Then
	   String jsonFromResponse = EntityUtils.toString(httpResponse.getEntity());
	   Assert.assertThat("[ERR-103] Invalid OFFER 'OFFER_BOGOF' for the product orange.", equalTo(jsonFromResponse));
	}
	
	
	@Test
	public void givenURIWithProudct_InvalidOfferSyntax_ExpectError()
	      throws ClientProtocolException, IOException {
	   // Given
	   HttpUriRequest request = new HttpGet( "http://localhost:8080/epos/products?productlist=orange,apple&offerlist=offer_bogof");
	 
	   // When
	   HttpResponse httpResponse = HttpClientBuilder.create().build().execute( request );

	   // Then
	   String jsonFromResponse = EntityUtils.toString(httpResponse.getEntity());
	   Assert.assertThat("[ERR-102] Invalid offer syntax for offer offer_bogof.", equalTo(jsonFromResponse));
	}
}
