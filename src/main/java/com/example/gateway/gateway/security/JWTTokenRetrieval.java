package com.example.gateway.gateway.security;

public final
class JWTTokenRetrieval {


    public
    JWTTokenRetrieval() {
    }
    

//    public
//    Map<String, String> getAccessToken() {
//
//        LinkedMultiValueMap<String, String> tokenMap = new LinkedMultiValueMap <>(  );
//        tokenMap.add( "grant_type", "client_credentials" );
//        tokenMap.add( "client_id","my-trusted-client" );
//        tokenMap.add( "scope","read write trust" );
//        tokenMap.add( "token_name","movieServiceToken" );
//
//        String s = Base64.getEncoder().encodeToString( "my-trusted-client:secret".getBytes() );
//
//        return WebClient.create().post().uri( uriBuilder -> URI.create( "http://localhost:9999/uaa/oauth/token" ) )
//                .headers( httpHeaders -> {
//                    httpHeaders.add( "content-type" , "application/x-www-form-urlencoded" );
//                    httpHeaders.add( "Authorization","Basic " + s );
//                } )
//                .body( BodyInserters.fromFormData( tokenMap ) ).retrieve().bodyToMono( Map.class ).block();
//    }
}