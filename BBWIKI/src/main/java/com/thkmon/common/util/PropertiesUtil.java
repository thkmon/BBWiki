package com.thkmon.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class PropertiesUtil {
	
	
    /**
     * UTF-8 인코딩 형식의 properties 파일을 읽어서 HashMap 객체로 만들어 리턴한다.
     * 
     * @param propFilePath
     * @return
     * @throws Exception
     */
    private static HashMap<String, String> readPropertiesFileCore(String propFilePath) throws Exception {
        if (propFilePath == null || propFilePath.length() == 0) {
            throw new Exception("PropertiesUtil readPropertiesFile : propFilePath == null || propFilePath.length() == 0");
        }

        File propFileObj = new File(propFilePath);
        if (!propFileObj.exists()) {
            throw new Exception("PropertiesUtil readPropertiesFile : propFileObj does not exists. [" + propFileObj.getAbsolutePath() + "]");
        }
        
        if (!propFileObj.canRead()) {
            throw new Exception("PropertiesUtil readPropertiesFile : propFileObj can not read. [" + propFileObj.getAbsolutePath() + "]");
        }

        HashMap<String, String> resultMap = new HashMap<String, String>();

        FileInputStream fileInputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;

        try {
            fileInputStream = new FileInputStream(propFileObj);
            inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
            bufferedReader = new BufferedReader(inputStreamReader);

            String oneLine = null;
            while ((oneLine = bufferedReader.readLine()) != null) {
                if (oneLine == null || oneLine.length() == 0) {
                    continue;
                }
                
                // 주석 무시
                if (oneLine.trim().startsWith("#")) {
                    continue;
                }
                
                int equalIndex = oneLine.indexOf("=");
                if (equalIndex < 0) {
                    continue;
                }
                
                // 좌측값(key값)만 trim 처리한다. 우측값(value값)은 의도적으로 공백이 포함될 수 있다고 판단한다.
                String leftText = oneLine.substring(0, equalIndex).trim();
                String rightText = oneLine.substring(equalIndex + 1);
                
                // 등호 좌측 텍스트가 존재하지 않을 경우 무시
                if (leftText.length() == 0) {
                    continue;
                }
                
                if (resultMap.get(leftText) != null) {
                	System.err.println("프로퍼티가 중복되어 발견되었습니다. 프로그램을 종료합니다. (Properties found duplicate. Exit the program.) : " + leftText);
                	System.exit(1);
                	break;
                }
                
                resultMap.put(leftText, rightText);
            }

        } catch (IOException e) {
            throw e;

        } catch (Exception e) {
            throw e;

        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
            } catch (Exception e) {
            } finally {
                bufferedReader = null;
            }

            try {
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
            } catch (IOException e) {
            } catch (Exception e) {
            } finally {
                inputStreamReader = null;
            }

            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
            } catch (Exception e) {
            } finally {
                fileInputStream = null;
            }
        }

        return resultMap;
    }
    
    
    public static HashMap<String, String> readPropertiesFile(String propFilePath) {
    	HashMap<String, String> resultMap = null;
    	
    	try {
    		resultMap = readPropertiesFileCore(propFilePath);
    	} catch (IOException e) {
    		e.printStackTrace();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	return resultMap;
    }
}