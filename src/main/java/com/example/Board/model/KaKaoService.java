package com.example.Board.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

@Service
public class KaKaoService {
	
	public String getToken(String code) throws IOException {
		
		String host = "https://kauth.kakao.com/oauth/token";
		URL url = new URL(host);
		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		String token = "";
		try {
			urlConnection.setRequestMethod("POST");
			urlConnection.setDoOutput(true);
			
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream()));
			StringBuilder sb = new StringBuilder();
			sb.append("grant_type=authorization_code");
			sb.append("&client_id=5c78ab9195c6750c34b1a2fd56ad0da1");
			sb.append("&redirect_uri=http://localhost:8080/api/member/kakao");
			sb.append("&code=" + code);
			
			bw.write(sb.toString());
			bw.flush();
			
			BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			String line = "";
			String result = "";
			while ((line = br.readLine()) != null) {
                result += line;
            }

            // json parsing
            JSONParser parser = new JSONParser();
            JSONObject elem = (JSONObject) parser.parse(result);

            String access_token = elem.get("access_token").toString();

            token = access_token;

            br.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return token;
	}
	
	public Map<String, Object> getUserInfo(String access_token) throws IOException {
		String host = "https://kapi.kakao.com/v2/user/me";
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			URL url = new URL(host);
			
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestProperty("Authorization", "Bearer " + access_token);
			urlConnection.setRequestMethod("GET");
			
			int responseCode = urlConnection.getResponseCode();
			System.out.println("responscCode : " + responseCode);
			
			BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			String line = "";
			String res = "";
			while((line = br.readLine()) != null) {
				res += line;
			}
			
			
			JSONParser parser = new JSONParser();
			JSONObject obj = (JSONObject) parser.parse(res);
			JSONObject kakao_account = (JSONObject) obj.get("kakao_account");
			JSONObject properties = (JSONObject) obj.get("properties");
			
			String id = obj.get("id").toString();
			String nickname = properties.get("nickname").toString();
			String email = kakao_account.get("email").toString();
			String gender = kakao_account.get("gender").toString().equals("male") ? "남" : "여";
			
			result.put("id", id);
			result.put("nickname", nickname);
			result.put("email", email);
			result.put("gender", gender);
			
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
}
