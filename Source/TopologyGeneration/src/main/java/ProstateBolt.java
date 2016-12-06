import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class ProstateBolt extends BaseBasicBolt {
    private static final Logger LOG = LoggerFactory.getLogger(ProstateBolt.class);
    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
        try {
            String s = tuple.getString(0);
          /*  String r[] = s.split("_"); */
            String filename = "Prostate";
  /*          String features = r[1];
*/
            double[] feature = fromString(s);
            Boolean check = checkProstate(feature);
            insertIntoMongoDB(check);
            basicOutputCollector.emit(new Values(filename,check));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("context","status"));
    }

    private static double[] fromString(String string) {
        String[] strings = string.split(" ");
        double result[] = new double[strings.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = Double.parseDouble(strings[i]);
        }
        return result;
    }

    public static void insertIntoMongoDB(Boolean check) {
        String API_KEY = "7IjZ5q5Y8cBtuOadP_iRf5b0__Qz13Vu";
        String DATABASE_NAME = "videoprocessing";
        String COLLECTION_NAME = "output";
        String urlString = "https://api.mlab.com/api/1/databases/" +
                DATABASE_NAME + "/collections/" + COLLECTION_NAME + "?apiKey=" + API_KEY;
        LOG.info(urlString);

        StringBuilder result = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Context", "Prostate");
            jsonObject.put("Decision", check);
            jsonObject.put("Timestamp", System.currentTimeMillis());
            writer.write(jsonObject.toString());
            LOG.info(jsonObject.toString());
            writer.close();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(urlConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Uploaded data to Mongo");

    }

    public Boolean checkProstate(double[] feature) {

  if (feature[72] <= 0.0) {
   if (feature[48] <= -5.0) {
    if (feature[2] <= -126.0) {
     if (feature[126] <= -128.0) {
      if (feature[58] <= -125.0) {
       if (feature[63] <= -125.0) {
        if (feature[24] <= -119.0) {
         if (feature[41] <= -96.0) {
          if (feature[51] <= -105.0) {
           if (feature[107] <= -62.0) {
            return false;
           }
           else if (feature[107] > -62.0){
            return false;
           }
          }
          else if (feature[51] > -105.0){
           if (feature[127] <= -128.0) {
            return false;
           }
           else if (feature[127] > -128.0){
            return false;
           }
          }
         }
         else if (feature[41] > -96.0){
          if (feature[108] <= -80.0) {
           if (feature[117] <= -115.0) {
            return false;
           }
           else if (feature[117] > -115.0){
            return false;
           }
          }
          else if (feature[108] > -80.0){
           return false;
          }
         }
        }
        else if (feature[24] > -119.0){
         if (feature[44] <= -94.0) {
          if (feature[86] <= -128.0) {
           return false;
          }
          else if (feature[86] > -128.0){
           if (feature[1] <= -99.0) {
            return false;
           }
           else if (feature[1] > -99.0){
            return false;
           }
          }
         }
         else if (feature[44] > -94.0){
          if (feature[103] <= -120.0) {
           if (feature[24] <= -78.0) {
            return false;
           }
           else if (feature[24] > -78.0){
            return false;
           }
          }
          else if (feature[103] > -120.0){
           if (feature[7] <= -46.0) {
            return false;
           }
           else if (feature[7] > -46.0){
            return false;
           }
          }
         }
        }
       }
       else if (feature[63] > -125.0){
        if (feature[92] <= -81.0) {
         if (feature[108] <= -126.0) {
          if (feature[90] <= -122.0) {
           if (feature[51] <= -111.0) {
            return false;
           }
           else if (feature[51] > -111.0){
            return false;
           }
          }
          else if (feature[90] > -122.0){
           if (feature[97] <= -45.0) {
            return false;
           }
           else if (feature[97] > -45.0){
            return false;
           }
          }
         }
         else if (feature[108] > -126.0){
          if (feature[67] <= -111.0) {
           if (feature[81] <= -112.0) {
            return false;
           }
           else if (feature[81] > -112.0){
            return false;
           }
          }
          else if (feature[67] > -111.0){
           if (feature[44] <= -88.0) {
            return false;
           }
           else if (feature[44] > -88.0){
            return false;
           }
          }
         }
        }
        else if (feature[92] > -81.0){
         if (feature[62] <= -128.0) {
          if (feature[98] <= -128.0) {
           return false;
          }
          else if (feature[98] > -128.0){
           if (feature[6] <= -127.0) {
            return false;
           }
           else if (feature[6] > -127.0){
            return false;
           }
          }
         }
         else if (feature[62] > -128.0){
          if (feature[11] <= -127.0) {
           if (feature[37] <= -124.0) {
            return false;
           }
           else if (feature[37] > -124.0){
            return false;
           }
          }
          else if (feature[11] > -127.0){
           if (feature[114] <= -111.0) {
            return false;
           }
           else if (feature[114] > -111.0){
            return false;
           }
          }
         }
        }
       }
      }
      else if (feature[58] > -125.0){
       if (feature[55] <= -104.0) {
        if (feature[41] <= -98.0) {
         if (feature[12] <= -26.0) {
          if (feature[82] <= -22.0) {
           if (feature[71] <= -21.0) {
            return false;
           }
           else if (feature[71] > -21.0){
            return false;
           }
          }
          else if (feature[82] > -22.0){
           if (feature[30] <= -83.0) {
            return false;
           }
           else if (feature[30] > -83.0){
            return false;
           }
          }
         }
         else if (feature[12] > -26.0){
          if (feature[0] <= -123.0) {
           return false;
          }
          else if (feature[0] > -123.0){
           return false;
          }
         }
        }
        else if (feature[41] > -98.0){
         if (feature[14] <= -119.0) {
          if (feature[28] <= -35.0) {
           if (feature[11] <= -100.0) {
            return false;
           }
           else if (feature[11] > -100.0){
            return false;
           }
          }
          else if (feature[28] > -35.0){
           if (feature[0] <= -115.0) {
            return false;
           }
           else if (feature[0] > -115.0){
            return false;
           }
          }
         }
         else if (feature[14] > -119.0){
          if (feature[58] <= -121.0) {
           if (feature[3] <= -125.0) {
            return false;
           }
           else if (feature[3] > -125.0){
            return false;
           }
          }
          else if (feature[58] > -121.0){
           if (feature[82] <= -40.0) {
            return false;
           }
           else if (feature[82] > -40.0){
            return false;
           }
          }
         }
        }
       }
       else if (feature[55] > -104.0){
        if (feature[15] <= -110.0) {
         if (feature[36] <= -100.0) {
          if (feature[24] <= -39.0) {
           if (feature[63] <= -121.0) {
            return false;
           }
           else if (feature[63] > -121.0){
            return false;
           }
          }
          else if (feature[24] > -39.0){
           if (feature[50] <= -123.0) {
            return false;
           }
           else if (feature[50] > -123.0){
            return false;
           }
          }
         }
         else if (feature[36] > -100.0){
          if (feature[31] <= -119.0) {
           if (feature[28] <= -118.0) {
            return false;
           }
           else if (feature[28] > -118.0){
            return false;
           }
          }
          else if (feature[31] > -119.0){
           if (feature[49] <= -107.0) {
            return false;
           }
           else if (feature[49] > -107.0){
            return false;
           }
          }
         }
        }
        else if (feature[15] > -110.0){
         if (feature[92] <= -89.0) {
          if (feature[36] <= -127.0) {
           if (feature[97] <= -128.0) {
            return false;
           }
           else if (feature[97] > -128.0){
            return false;
           }
          }
          else if (feature[36] > -127.0){
           if (feature[62] <= -116.0) {
            return false;
           }
           else if (feature[62] > -116.0){
            return false;
           }
          }
         }
         else if (feature[92] > -89.0){
          if (feature[8] <= -116.0) {
           if (feature[101] <= -118.0) {
            return false;
           }
           else if (feature[101] > -118.0){
            return false;
           }
          }
          else if (feature[8] > -116.0){
           if (feature[41] <= -112.0) {
            return false;
           }
           else if (feature[41] > -112.0){
            return false;
           }
          }
         }
        }
       }
      }
     }
     else if (feature[126] > -128.0){
      if (feature[14] <= -126.0) {
       if (feature[100] <= -15.0) {
        if (feature[6] <= -121.0) {
         if (feature[59] <= -126.0) {
          if (feature[105] <= -54.0) {
           if (feature[122] <= -128.0) {
            return false;
           }
           else if (feature[122] > -128.0){
            return false;
           }
          }
          else if (feature[105] > -54.0){
           if (feature[4] <= -115.0) {
            return false;
           }
           else if (feature[4] > -115.0){
            return false;
           }
          }
         }
         else if (feature[59] > -126.0){
          if (feature[46] <= -128.0) {
           if (feature[111] <= -122.0) {
            return false;
           }
           else if (feature[111] > -122.0){
            return false;
           }
          }
          else if (feature[46] > -128.0){
           if (feature[15] <= -121.0) {
            return false;
           }
           else if (feature[15] > -121.0){
            return false;
           }
          }
         }
        }
        else if (feature[6] > -121.0){
         if (feature[6] <= -80.0) {
          if (feature[51] <= -14.0) {
           if (feature[39] <= -128.0) {
            return false;
           }
           else if (feature[39] > -128.0){
            return false;
           }
          }
          else if (feature[51] > -14.0){
           if (feature[15] <= -30.0) {
            return false;
           }
           else if (feature[15] > -30.0){
            return false;
           }
          }
         }
         else if (feature[6] > -80.0){
          if (feature[0] <= -38.0) {
           return false;
          }
          else if (feature[0] > -38.0){
           if (feature[1] <= -94.0) {
            return false;
           }
           else if (feature[1] > -94.0){
            return false;
           }
          }
         }
        }
       }
       else if (feature[100] > -15.0){
        if (feature[12] <= -67.0) {
         if (feature[60] <= -96.0) {
          if (feature[113] <= -120.0) {
           if (feature[0] <= -122.0) {
            return false;
           }
           else if (feature[0] > -122.0){
            return false;
           }
          }
          else if (feature[113] > -120.0){
           if (feature[89] <= -118.0) {
            return false;
           }
           else if (feature[89] > -118.0){
            return false;
           }
          }
         }
         else if (feature[60] > -96.0){
          return false;
         }
        }
        else if (feature[12] > -67.0){
         if (feature[8] <= -128.0) {
          return false;
         }
         else if (feature[8] > -128.0){
          return false;
         }
        }
       }
      }
      else if (feature[14] > -126.0){
       if (feature[97] <= -126.0) {
        if (feature[103] <= -117.0) {
         if (feature[36] <= -115.0) {
          if (feature[83] <= -107.0) {
           if (feature[127] <= -120.0) {
            return false;
           }
           else if (feature[127] > -120.0){
            return false;
           }
          }
          else if (feature[83] > -107.0){
           if (feature[38] <= -128.0) {
            return false;
           }
           else if (feature[38] > -128.0){
            return false;
           }
          }
         }
         else if (feature[36] > -115.0){
          if (feature[71] <= -126.0) {
           if (feature[4] <= -87.0) {
            return false;
           }
           else if (feature[4] > -87.0){
            return false;
           }
          }
          else if (feature[71] > -126.0){
           if (feature[6] <= -127.0) {
            return false;
           }
           else if (feature[6] > -127.0){
            return false;
           }
          }
         }
        }
        else if (feature[103] > -117.0){
         if (feature[116] <= -127.0) {
          if (feature[6] <= -124.0) {
           if (feature[80] <= -99.0) {
            return false;
           }
           else if (feature[80] > -99.0){
            return false;
           }
          }
          else if (feature[6] > -124.0){
           if (feature[24] <= -128.0) {
            return false;
           }
           else if (feature[24] > -128.0){
            return false;
           }
          }
         }
         else if (feature[116] > -127.0){
          if (feature[121] <= -126.0) {
           if (feature[70] <= -96.0) {
            return false;
           }
           else if (feature[70] > -96.0){
            return false;
           }
          }
          else if (feature[121] > -126.0){
           if (feature[36] <= -118.0) {
            return false;
           }
           else if (feature[36] > -118.0){
            return false;
           }
          }
         }
        }
       }
       else if (feature[97] > -126.0){
        if (feature[110] <= -125.0) {
         if (feature[7] <= -116.0) {
          if (feature[113] <= -8.0) {
           if (feature[41] <= -115.0) {
            return false;
           }
           else if (feature[41] > -115.0){
            return false;
           }
          }
          else if (feature[113] > -8.0){
           if (feature[44] <= -126.0) {
            return false;
           }
           else if (feature[44] > -126.0){
            return false;
           }
          }
         }
         else if (feature[7] > -116.0){
          if (feature[86] <= -124.0) {
           if (feature[122] <= -113.0) {
            return false;
           }
           else if (feature[122] > -113.0){
            return false;
           }
          }
          else if (feature[86] > -124.0){
           if (feature[48] <= -7.0) {
            return false;
           }
           else if (feature[48] > -7.0){
            return false;
           }
          }
         }
        }
        else if (feature[110] > -125.0){
         if (feature[26] <= -126.0) {
          if (feature[3] <= -127.0) {
           if (feature[70] <= -128.0) {
            return false;
           }
           else if (feature[70] > -128.0){
            return false;
           }
          }
          else if (feature[3] > -127.0){
           if (feature[89] <= -128.0) {
            return false;
           }
           else if (feature[89] > -128.0){
            return false;
           }
          }
         }
         else if (feature[26] > -126.0){
          if (feature[75] <= -128.0) {
           if (feature[84] <= -126.0) {
            return false;
           }
           else if (feature[84] > -126.0){
            return false;
           }
          }
          else if (feature[75] > -128.0){
           if (feature[78] <= -126.0) {
            return false;
           }
           else if (feature[78] > -126.0){
            return false;
           }
          }
         }
        }
       }
      }
     }
    }
    else if (feature[2] > -126.0){
     if (feature[37] <= -127.0) {
      if (feature[63] <= -125.0) {
       if (feature[29] <= -126.0) {
        if (feature[26] <= -102.0) {
         if (feature[20] <= -128.0) {
          if (feature[93] <= -128.0) {
           return false;
          }
          else if (feature[93] > -128.0){
           if (feature[14] <= -122.0) {
            return false;
           }
           else if (feature[14] > -122.0){
            return false;
           }
          }
         }
         else if (feature[20] > -128.0){
          if (feature[7] <= -91.0) {
           if (feature[17] <= -31.0) {
            return false;
           }
           else if (feature[17] > -31.0){
            return false;
           }
          }
          else if (feature[7] > -91.0){
           if (feature[18] <= -123.0) {
            return false;
           }
           else if (feature[18] > -123.0){
            return false;
           }
          }
         }
        }
        else if (feature[26] > -102.0){
         if (feature[31] <= -127.0) {
          if (feature[91] <= -128.0) {
           return false;
          }
          else if (feature[91] > -128.0){
           if (feature[123] <= -75.0) {
            return false;
           }
           else if (feature[123] > -75.0){
            return false;
           }
          }
         }
         else if (feature[31] > -127.0){
          if (feature[66] <= -125.0) {
           if (feature[107] <= -125.0) {
            return false;
           }
           else if (feature[107] > -125.0){
            return false;
           }
          }
          else if (feature[66] > -125.0){
           if (feature[69] <= -125.0) {
            return false;
           }
           else if (feature[69] > -125.0){
            return false;
           }
          }
         }
        }
       }
       else if (feature[29] > -126.0){
        if (feature[0] <= -120.0) {
         if (feature[45] <= -127.0) {
          if (feature[11] <= -100.0) {
           if (feature[51] <= -83.0) {
            return false;
           }
           else if (feature[51] > -83.0){
            return false;
           }
          }
          else if (feature[11] > -100.0){
           if (feature[79] <= -109.0) {
            return false;
           }
           else if (feature[79] > -109.0){
            return false;
           }
          }
         }
         else if (feature[45] > -127.0){
          if (feature[99] <= -125.0) {
           if (feature[106] <= -106.0) {
            return false;
           }
           else if (feature[106] > -106.0){
            return false;
           }
          }
          else if (feature[99] > -125.0){
           if (feature[126] <= -107.0) {
            return false;
           }
           else if (feature[126] > -107.0){
            return false;
           }
          }
         }
        }
        else if (feature[0] > -120.0){
         if (feature[101] <= -128.0) {
          if (feature[55] <= -110.0) {
           if (feature[124] <= -61.0) {
            return false;
           }
           else if (feature[124] > -61.0){
            return false;
           }
          }
          else if (feature[55] > -110.0){
           if (feature[58] <= -126.0) {
            return false;
           }
           else if (feature[58] > -126.0){
            return false;
           }
          }
         }
         else if (feature[101] > -128.0){
          if (feature[27] <= -105.0) {
           if (feature[33] <= -123.0) {
            return false;
           }
           else if (feature[33] > -123.0){
            return false;
           }
          }
          else if (feature[27] > -105.0){
           if (feature[124] <= -120.0) {
            return false;
           }
           else if (feature[124] > -120.0){
            return false;
           }
          }
         }
        }
       }
      }
      else if (feature[63] > -125.0){
       if (feature[7] <= -126.0) {
        if (feature[4] <= -118.0) {
         if (feature[30] <= -119.0) {
          if (feature[1] <= -124.0) {
           if (feature[87] <= -86.0) {
            return false;
           }
           else if (feature[87] > -86.0){
            return false;
           }
          }
          else if (feature[1] > -124.0){
           if (feature[93] <= -104.0) {
            return false;
           }
           else if (feature[93] > -104.0){
            return false;
           }
          }
         }
         else if (feature[30] > -119.0){
          if (feature[69] <= -123.0) {
           if (feature[62] <= -10.0) {
            return false;
           }
           else if (feature[62] > -10.0){
            return false;
           }
          }
          else if (feature[69] > -123.0){
           if (feature[56] <= -127.0) {
            return false;
           }
           else if (feature[56] > -127.0){
            return false;
           }
          }
         }
        }
        else if (feature[4] > -118.0){
         if (feature[34] <= -119.0) {
          if (feature[8] <= -120.0) {
           if (feature[14] <= -128.0) {
            return false;
           }
           else if (feature[14] > -128.0){
            return false;
           }
          }
          else if (feature[8] > -120.0){
           if (feature[19] <= -114.0) {
            return false;
           }
           else if (feature[19] > -114.0){
            return false;
           }
          }
         }
         else if (feature[34] > -119.0){
          if (feature[39] <= -128.0) {
           if (feature[27] <= -118.0) {
            return false;
           }
           else if (feature[27] > -118.0){
            return false;
           }
          }
          else if (feature[39] > -128.0){
           if (feature[49] <= -123.0) {
            return false;
           }
           else if (feature[49] > -123.0){
            return false;
           }
          }
         }
        }
       }
       else if (feature[7] > -126.0){
        if (feature[25] <= -127.0) {
         if (feature[127] <= -128.0) {
          if (feature[86] <= -128.0) {
           if (feature[17] <= -120.0) {
            return false;
           }
           else if (feature[17] > -120.0){
            return false;
           }
          }
          else if (feature[86] > -128.0){
           if (feature[104] <= -116.0) {
            return false;
           }
           else if (feature[104] > -116.0){
            return false;
           }
          }
         }
         else if (feature[127] > -128.0){
          if (feature[89] <= -118.0) {
           if (feature[84] <= -10.0) {
            return false;
           }
           else if (feature[84] > -10.0){
            return false;
           }
          }
          else if (feature[89] > -118.0){
           if (feature[32] <= -19.0) {
            return false;
           }
           else if (feature[32] > -19.0){
            return false;
           }
          }
         }
        }
        else if (feature[25] > -127.0){
         if (feature[102] <= -125.0) {
          if (feature[28] <= -105.0) {
           if (feature[8] <= -25.0) {
            return false;
           }
           else if (feature[8] > -25.0){
            return false;
           }
          }
          else if (feature[28] > -105.0){
           if (feature[90] <= -126.0) {
            return false;
           }
           else if (feature[90] > -126.0){
            return false;
           }
          }
         }
         else if (feature[102] > -125.0){
          if (feature[53] <= -4.0) {
           if (feature[125] <= -127.0) {
            return false;
           }
           else if (feature[125] > -127.0){
            return false;
           }
          }
          else if (feature[53] > -4.0){
           if (feature[26] <= -122.0) {
            return false;
           }
           else if (feature[26] > -122.0){
            return false;
           }
          }
         }
        }
       }
      }
     }
     else if (feature[37] > -127.0){
      if (feature[25] <= -127.0) {
       if (feature[124] <= -128.0) {
        if (feature[40] <= -11.0) {
         if (feature[47] <= -126.0) {
          if (feature[29] <= -111.0) {
           return false;
          }
          else if (feature[29] > -111.0){
           if (feature[10] <= -72.0) {
            return false;
           }
           else if (feature[10] > -72.0){
            return false;
           }
          }
         }
         else if (feature[47] > -126.0){
          if (feature[113] <= -116.0) {
           if (feature[64] <= -105.0) {
            return false;
           }
           else if (feature[64] > -105.0){
            return false;
           }
          }
          else if (feature[113] > -116.0){
           if (feature[89] <= -117.0) {
            return false;
           }
           else if (feature[89] > -117.0){
            return false;
           }
          }
         }
        }
        else if (feature[40] > -11.0){
         if (feature[121] <= -81.0) {
          if (feature[70] <= -118.0) {
           if (feature[34] <= -102.0) {
            return false;
           }
           else if (feature[34] > -102.0){
            return false;
           }
          }
          else if (feature[70] > -118.0){
           if (feature[45] <= -100.0) {
            return false;
           }
           else if (feature[45] > -100.0){
            return false;
           }
          }
         }
         else if (feature[121] > -81.0){
          if (feature[57] <= -99.0) {
           if (feature[20] <= -100.0) {
            return false;
           }
           else if (feature[20] > -100.0){
            return false;
           }
          }
          else if (feature[57] > -99.0){
           return false;
          }
         }
        }
       }
       else if (feature[124] > -128.0){
        if (feature[102] <= -127.0) {
         if (feature[78] <= -121.0) {
          if (feature[74] <= -113.0) {
           if (feature[26] <= -113.0) {
            return false;
           }
           else if (feature[26] > -113.0){
            return false;
           }
          }
          else if (feature[74] > -113.0){
           if (feature[8] <= -123.0) {
            return false;
           }
           else if (feature[8] > -123.0){
            return false;
           }
          }
         }
         else if (feature[78] > -121.0){
          if (feature[98] <= -124.0) {
           if (feature[22] <= -102.0) {
            return false;
           }
           else if (feature[22] > -102.0){
            return false;
           }
          }
          else if (feature[98] > -124.0){
           if (feature[106] <= -127.0) {
            return false;
           }
           else if (feature[106] > -127.0){
            return false;
           }
          }
         }
        }
        else if (feature[102] > -127.0){
         if (feature[6] <= -128.0) {
          if (feature[67] <= -125.0) {
           if (feature[77] <= -119.0) {
            return false;
           }
           else if (feature[77] > -119.0){
            return false;
           }
          }
          else if (feature[67] > -125.0){
           if (feature[96] <= -125.0) {
            return false;
           }
           else if (feature[96] > -125.0){
            return false;
           }
          }
         }
         else if (feature[6] > -128.0){
          if (feature[0] <= -124.0) {
           if (feature[87] <= -36.0) {
            return false;
           }
           else if (feature[87] > -36.0){
            return false;
           }
          }
          else if (feature[0] > -124.0){
           if (feature[29] <= -128.0) {
            return false;
           }
           else if (feature[29] > -128.0){
            return false;
           }
          }
         }
        }
       }
      }
      else if (feature[25] > -127.0){
       if (feature[126] <= -128.0) {
        if (feature[122] <= -124.0) {
         if (feature[120] <= -124.0) {
          if (feature[7] <= -124.0) {
           if (feature[69] <= -122.0) {
            return false;
           }
           else if (feature[69] > -122.0){
            return false;
           }
          }
          else if (feature[7] > -124.0){
           if (feature[120] <= -128.0) {
            return false;
           }
           else if (feature[120] > -128.0){
            return false;
           }
          }
         }
         else if (feature[120] > -124.0){
          if (feature[114] <= -124.0) {
           if (feature[16] <= -103.0) {
            return false;
           }
           else if (feature[16] > -103.0){
            return false;
           }
          }
          else if (feature[114] > -124.0){
           if (feature[49] <= -124.0) {
            return false;
           }
           else if (feature[49] > -124.0){
            return false;
           }
          }
         }
        }
        else if (feature[122] > -124.0){
         if (feature[20] <= -124.0) {
          if (feature[102] <= -121.0) {
           if (feature[29] <= -128.0) {
            return false;
           }
           else if (feature[29] > -128.0){
            return false;
           }
          }
          else if (feature[102] > -121.0){
           if (feature[98] <= -78.0) {
            return false;
           }
           else if (feature[98] > -78.0){
            return false;
           }
          }
         }
         else if (feature[20] > -124.0){
          if (feature[34] <= -127.0) {
           if (feature[53] <= -86.0) {
            return false;
           }
           else if (feature[53] > -86.0){
            return false;
           }
          }
          else if (feature[34] > -127.0){
           if (feature[61] <= -114.0) {
            return false;
           }
           else if (feature[61] > -114.0){
            return false;
           }
          }
         }
        }
       }
       else if (feature[126] > -128.0){
        if (feature[28] <= -126.0) {
         if (feature[31] <= -121.0) {
          if (feature[25] <= -96.0) {
           if (feature[20] <= -127.0) {
            return false;
           }
           else if (feature[20] > -127.0){
            return false;
           }
          }
          else if (feature[25] > -96.0){
           if (feature[60] <= -128.0) {
            return false;
           }
           else if (feature[60] > -128.0){
            return false;
           }
          }
         }
         else if (feature[31] > -121.0){
          if (feature[40] <= -4.0) {
           if (feature[36] <= -128.0) {
            return false;
           }
           else if (feature[36] > -128.0){
            return false;
           }
          }
          else if (feature[40] > -4.0){
           if (feature[20] <= -123.0) {
            return false;
           }
           else if (feature[20] > -123.0){
            return false;
           }
          }
         }
        }
        else if (feature[28] > -126.0){
         if (feature[67] <= -128.0) {
          if (feature[84] <= -122.0) {
           if (feature[6] <= -126.0) {
            return false;
           }
           else if (feature[6] > -126.0){
            return false;
           }
          }
          else if (feature[84] > -122.0){
           if (feature[102] <= -126.0) {
            return false;
           }
           else if (feature[102] > -126.0){
            return false;
           }
          }
         }
         else if (feature[67] > -128.0){
          if (feature[7] <= -128.0) {
           if (feature[117] <= -127.0) {
            return false;
           }
           else if (feature[117] > -127.0){
            return false;
           }
          }
          else if (feature[7] > -128.0){
           if (feature[62] <= -124.0) {
            return false;
           }
           else if (feature[62] > -124.0){
            return false;
           }
          }
         }
        }
       }
      }
     }
    }
   }
   else if (feature[48] > -5.0){
    if (feature[44] <= -112.0) {
     if (feature[50] <= -127.0) {
      if (feature[62] <= -128.0) {
       if (feature[61] <= -124.0) {
        if (feature[57] <= -99.0) {
         if (feature[15] <= -90.0) {
          if (feature[53] <= -115.0) {
           if (feature[93] <= -122.0) {
            return false;
           }
           else if (feature[93] > -122.0){
            return false;
           }
          }
          else if (feature[53] > -115.0){
           return false;
          }
         }
         else if (feature[15] > -90.0){
          if (feature[78] <= -118.0) {
           if (feature[35] <= -109.0) {
            return false;
           }
           else if (feature[35] > -109.0){
            return false;
           }
          }
          else if (feature[78] > -118.0){
           if (feature[50] <= -128.0) {
            return false;
           }
           else if (feature[50] > -128.0){
            return false;
           }
          }
         }
        }
        else if (feature[57] > -99.0){
         if (feature[107] <= -125.0) {
          if (feature[81] <= -115.0) {
           if (feature[1] <= -94.0) {
            return false;
           }
           else if (feature[1] > -94.0){
            return false;
           }
          }
          else if (feature[81] > -115.0){
           if (feature[112] <= -27.0) {
            return false;
           }
           else if (feature[112] > -27.0){
            return false;
           }
          }
         }
         else if (feature[107] > -125.0){
          if (feature[10] <= -127.0) {
           return false;
          }
          else if (feature[10] > -127.0){
           if (feature[2] <= -122.0) {
            return false;
           }
           else if (feature[2] > -122.0){
            return false;
           }
          }
         }
        }
       }
       else if (feature[61] > -124.0){
        if (feature[58] <= -128.0) {
         if (feature[107] <= -123.0) {
          if (feature[56] <= -97.0) {
           if (feature[0] <= -125.0) {
            return false;
           }
           else if (feature[0] > -125.0){
            return false;
           }
          }
          else if (feature[56] > -97.0){
           if (feature[5] <= -116.0) {
            return false;
           }
           else if (feature[5] > -116.0){
            return false;
           }
          }
         }
         else if (feature[107] > -123.0){
          if (feature[48] <= 21.0) {
           if (feature[79] <= -85.0) {
            return false;
           }
           else if (feature[79] > -85.0){
            return false;
           }
          }
          else if (feature[48] > 21.0){
           if (feature[1] <= -126.0) {
            return false;
           }
           else if (feature[1] > -126.0){
            return false;
           }
          }
         }
        }
        else if (feature[58] > -128.0){
         if (feature[63] <= -126.0) {
          if (feature[1] <= -115.0) {
           if (feature[19] <= -127.0) {
            return false;
           }
           else if (feature[19] > -127.0){
            return false;
           }
          }
          else if (feature[1] > -115.0){
           if (feature[40] <= -22.0) {
            return false;
           }
           else if (feature[40] > -22.0){
            return false;
           }
          }
         }
         else if (feature[63] > -126.0){
          if (feature[20] <= -124.0) {
           return false;
          }
          else if (feature[20] > -124.0){
           if (feature[0] <= -128.0) {
            return false;
           }
           else if (feature[0] > -128.0){
            return false;
           }
          }
         }
        }
       }
      }
      else if (feature[62] > -128.0){
       if (feature[120] <= -90.0) {
        if (feature[15] <= -110.0) {
         if (feature[25] <= -128.0) {
          if (feature[20] <= -58.0) {
           if (feature[57] <= -121.0) {
            return false;
           }
           else if (feature[57] > -121.0){
            return false;
           }
          }
          else if (feature[20] > -58.0){
           if (feature[13] <= -127.0) {
            return false;
           }
           else if (feature[13] > -127.0){
            return false;
           }
          }
         }
         else if (feature[25] > -128.0){
          if (feature[100] <= -125.0) {
           if (feature[76] <= -124.0) {
            return false;
           }
           else if (feature[76] > -124.0){
            return false;
           }
          }
          else if (feature[100] > -125.0){
           if (feature[63] <= -126.0) {
            return false;
           }
           else if (feature[63] > -126.0){
            return false;
           }
          }
         }
        }
        else if (feature[15] > -110.0){
         if (feature[124] <= -121.0) {
          if (feature[63] <= -17.0) {
           if (feature[7] <= -91.0) {
            return false;
           }
           else if (feature[7] > -91.0){
            return false;
           }
          }
          else if (feature[63] > -17.0){
           return false;
          }
         }
         else if (feature[124] > -121.0){
          if (feature[71] <= -104.0) {
           if (feature[9] <= -118.0) {
            return false;
           }
           else if (feature[9] > -118.0){
            return false;
           }
          }
          else if (feature[71] > -104.0){
           if (feature[23] <= -78.0) {
            return false;
           }
           else if (feature[23] > -78.0){
            return false;
           }
          }
         }
        }
       }
       else if (feature[120] > -90.0){
        if (feature[73] <= -115.0) {
         if (feature[49] <= -110.0) {
          if (feature[119] <= -115.0) {
           if (feature[36] <= -5.0) {
            return false;
           }
           else if (feature[36] > -5.0){
            return false;
           }
          }
          else if (feature[119] > -115.0){
           if (feature[88] <= -58.0) {
            return false;
           }
           else if (feature[88] > -58.0){
            return false;
           }
          }
         }
         else if (feature[49] > -110.0){
          if (feature[66] <= -128.0) {
           if (feature[112] <= -110.0) {
            return false;
           }
           else if (feature[112] > -110.0){
            return false;
           }
          }
          else if (feature[66] > -128.0){
           if (feature[1] <= -118.0) {
            return false;
           }
           else if (feature[1] > -118.0){
            return false;
           }
          }
         }
        }
        else if (feature[73] > -115.0){
         if (feature[45] <= -118.0) {
          if (feature[59] <= -124.0) {
           if (feature[115] <= -123.0) {
            return false;
           }
           else if (feature[115] > -123.0){
            return false;
           }
          }
          else if (feature[59] > -124.0){
           if (feature[90] <= -128.0) {
            return false;
           }
           else if (feature[90] > -128.0){
            return false;
           }
          }
         }
         else if (feature[45] > -118.0){
          if (feature[37] <= -124.0) {
           if (feature[23] <= -47.0) {
            return false;
           }
           else if (feature[23] > -47.0){
            return false;
           }
          }
          else if (feature[37] > -124.0){
           if (feature[120] <= -16.0) {
            return false;
           }
           else if (feature[120] > -16.0){
            return false;
           }
          }
         }
        }
       }
      }
     }
     else if (feature[50] > -127.0){
      if (feature[64] <= -127.0) {
       if (feature[55] <= -71.0) {
        if (feature[40] <= -105.0) {
         if (feature[67] <= -54.0) {
          if (feature[121] <= -128.0) {
           return false;
          }
          else if (feature[121] > -128.0){
           if (feature[100] <= -125.0) {
            return false;
           }
           else if (feature[100] > -125.0){
            return false;
           }
          }
         }
         else if (feature[67] > -54.0){
          if (feature[3] <= -111.0) {
           return false;
          }
          else if (feature[3] > -111.0){
           if (feature[1] <= -128.0) {
            return false;
           }
           else if (feature[1] > -128.0){
            return false;
           }
          }
         }
        }
        else if (feature[40] > -105.0){
         if (feature[19] <= -120.0) {
          if (feature[73] <= -127.0) {
           if (feature[99] <= -127.0) {
            return false;
           }
           else if (feature[99] > -127.0){
            return false;
           }
          }
          else if (feature[73] > -127.0){
           if (feature[113] <= -126.0) {
            return false;
           }
           else if (feature[113] > -126.0){
            return false;
           }
          }
         }
         else if (feature[19] > -120.0){
          if (feature[33] <= -98.0) {
           if (feature[93] <= -25.0) {
            return false;
           }
           else if (feature[93] > -25.0){
            return false;
           }
          }
          else if (feature[33] > -98.0){
           if (feature[0] <= -118.0) {
            return false;
           }
           else if (feature[0] > -118.0){
            return false;
           }
          }
         }
        }
       }
       else if (feature[55] > -71.0){
        if (feature[80] <= -3.0) {
         if (feature[93] <= -111.0) {
          if (feature[49] <= -82.0) {
           if (feature[21] <= -117.0) {
            return false;
           }
           else if (feature[21] > -117.0){
            return false;
           }
          }
          else if (feature[49] > -82.0){
           if (feature[97] <= -126.0) {
            return false;
           }
           else if (feature[97] > -126.0){
            return false;
           }
          }
         }
         else if (feature[93] > -111.0){
          if (feature[16] <= -48.0) {
           if (feature[0] <= -124.0) {
            return false;
           }
           else if (feature[0] > -124.0){
            return false;
           }
          }
          else if (feature[16] > -48.0){
           if (feature[16] <= -27.0) {
            return false;
           }
           else if (feature[16] > -27.0){
            return false;
           }
          }
         }
        }
        else if (feature[80] > -3.0){
         if (feature[95] <= -108.0) {
          if (feature[119] <= -123.0) {
           if (feature[125] <= -119.0) {
            return false;
           }
           else if (feature[125] > -119.0){
            return false;
           }
          }
          else if (feature[119] > -123.0){
           if (feature[105] <= -100.0) {
            return false;
           }
           else if (feature[105] > -100.0){
            return false;
           }
          }
         }
         else if (feature[95] > -108.0){
          if (feature[24] <= -126.0) {
           if (feature[5] <= -128.0) {
            return false;
           }
           else if (feature[5] > -128.0){
            return false;
           }
          }
          else if (feature[24] > -126.0){
           if (feature[87] <= -100.0) {
            return false;
           }
           else if (feature[87] > -100.0){
            return false;
           }
          }
         }
        }
       }
      }
      else if (feature[64] > -127.0){
       if (feature[3] <= -125.0) {
        if (feature[65] <= -124.0) {
         if (feature[14] <= -65.0) {
          if (feature[32] <= -107.0) {
           if (feature[79] <= -12.0) {
            return false;
           }
           else if (feature[79] > -12.0){
            return false;
           }
          }
          else if (feature[32] > -107.0){
           if (feature[118] <= -110.0) {
            return false;
           }
           else if (feature[118] > -110.0){
            return false;
           }
          }
         }
         else if (feature[14] > -65.0){
          if (feature[10] <= -115.0) {
           return false;
          }
          else if (feature[10] > -115.0){
           return false;
          }
         }
        }
        else if (feature[65] > -124.0){
         if (feature[14] <= -126.0) {
          if (feature[83] <= -110.0) {
           if (feature[87] <= -9.0) {
            return false;
           }
           else if (feature[87] > -9.0){
            return false;
           }
          }
          else if (feature[83] > -110.0){
           if (feature[61] <= -121.0) {
            return false;
           }
           else if (feature[61] > -121.0){
            return false;
           }
          }
         }
         else if (feature[14] > -126.0){
          if (feature[73] <= -121.0) {
           if (feature[0] <= -94.0) {
            return false;
           }
           else if (feature[0] > -94.0){
            return false;
           }
          }
          else if (feature[73] > -121.0){
           if (feature[102] <= -124.0) {
            return false;
           }
           else if (feature[102] > -124.0){
            return false;
           }
          }
         }
        }
       }
       else if (feature[3] > -125.0){
        if (feature[101] <= -126.0) {
         if (feature[97] <= -128.0) {
          if (feature[69] <= -124.0) {
           if (feature[114] <= -88.0) {
            return false;
           }
           else if (feature[114] > -88.0){
            return false;
           }
          }
          else if (feature[69] > -124.0){
           if (feature[9] <= -94.0) {
            return false;
           }
           else if (feature[9] > -94.0){
            return false;
           }
          }
         }
         else if (feature[97] > -128.0){
          if (feature[48] <= 2.0) {
           if (feature[57] <= -125.0) {
            return false;
           }
           else if (feature[57] > -125.0){
            return false;
           }
          }
          else if (feature[48] > 2.0){
           if (feature[112] <= -83.0) {
            return false;
           }
           else if (feature[112] > -83.0){
            return false;
           }
          }
         }
        }
        else if (feature[101] > -126.0){
         if (feature[22] <= -110.0) {
          if (feature[123] <= -126.0) {
           if (feature[5] <= -111.0) {
            return false;
           }
           else if (feature[5] > -111.0){
            return false;
           }
          }
          else if (feature[123] > -126.0){
           if (feature[5] <= -117.0) {
            return false;
           }
           else if (feature[5] > -117.0){
            return false;
           }
          }
         }
         else if (feature[22] > -110.0){
          if (feature[33] <= -126.0) {
           if (feature[48] <= 5.0) {
            return false;
           }
           else if (feature[48] > 5.0){
            return false;
           }
          }
          else if (feature[33] > -126.0){
           if (feature[30] <= -123.0) {
            return false;
           }
           else if (feature[30] > -123.0){
            return false;
           }
          }
         }
        }
       }
      }
     }
    }
    else if (feature[44] > -112.0){
     if (feature[112] <= 15.0) {
      if (feature[102] <= -127.0) {
       if (feature[52] <= -85.0) {
        if (feature[127] <= -128.0) {
         if (feature[55] <= -119.0) {
          if (feature[69] <= -127.0) {
           return false;
          }
          else if (feature[69] > -127.0){
           if (feature[0] <= -128.0) {
            return false;
           }
           else if (feature[0] > -128.0){
            return false;
           }
          }
         }
         else if (feature[55] > -119.0){
          if (feature[122] <= -17.0) {
           if (feature[119] <= -71.0) {
            return false;
           }
           else if (feature[119] > -71.0){
            return false;
           }
          }
          else if (feature[122] > -17.0){
           if (feature[98] <= -108.0) {
            return false;
           }
           else if (feature[98] > -108.0){
            return false;
           }
          }
         }
        }
        else if (feature[127] > -128.0){
         if (feature[74] <= -122.0) {
          if (feature[25] <= -127.0) {
           if (feature[105] <= -84.0) {
            return false;
           }
           else if (feature[105] > -84.0){
            return false;
           }
          }
          else if (feature[25] > -127.0){
           if (feature[3] <= -107.0) {
            return false;
           }
           else if (feature[3] > -107.0){
            return false;
           }
          }
         }
         else if (feature[74] > -122.0){
          if (feature[58] <= -125.0) {
           if (feature[88] <= -94.0) {
            return false;
           }
           else if (feature[88] > -94.0){
            return false;
           }
          }
          else if (feature[58] > -125.0){
           if (feature[10] <= -126.0) {
            return false;
           }
           else if (feature[10] > -126.0){
            return false;
           }
          }
         }
        }
       }
       else if (feature[52] > -85.0){
        if (feature[56] <= -110.0) {
         if (feature[126] <= -128.0) {
          if (feature[104] <= -52.0) {
           if (feature[17] <= -97.0) {
            return false;
           }
           else if (feature[17] > -97.0){
            return false;
           }
          }
          else if (feature[104] > -52.0){
           if (feature[74] <= -127.0) {
            return false;
           }
           else if (feature[74] > -127.0){
            return false;
           }
          }
         }
         else if (feature[126] > -128.0){
          if (feature[85] <= -117.0) {
           if (feature[125] <= -119.0) {
            return false;
           }
           else if (feature[125] > -119.0){
            return false;
           }
          }
          else if (feature[85] > -117.0){
           if (feature[112] <= -123.0) {
            return false;
           }
           else if (feature[112] > -123.0){
            return false;
           }
          }
         }
        }
        else if (feature[56] > -110.0){
         if (feature[81] <= -98.0) {
          if (feature[31] <= -102.0) {
           if (feature[14] <= -106.0) {
            return false;
           }
           else if (feature[14] > -106.0){
            return false;
           }
          }
          else if (feature[31] > -102.0){
           if (feature[0] <= -123.0) {
            return false;
           }
           else if (feature[0] > -123.0){
            return false;
           }
          }
         }
         else if (feature[81] > -98.0){
          if (feature[77] <= -123.0) {
           return false;
          }
          else if (feature[77] > -123.0){
           if (feature[6] <= -116.0) {
            return false;
           }
           else if (feature[6] > -116.0){
            return false;
           }
          }
         }
        }
       }
      }
      else if (feature[102] > -127.0){
       if (feature[105] <= -123.0) {
        if (feature[97] <= -127.0) {
         if (feature[5] <= -123.0) {
          if (feature[40] <= -63.0) {
           if (feature[118] <= -33.0) {
            return false;
           }
           else if (feature[118] > -33.0){
            return false;
           }
          }
          else if (feature[40] > -63.0){
           if (feature[101] <= -106.0) {
            return false;
           }
           else if (feature[101] > -106.0){
            return false;
           }
          }
         }
         else if (feature[5] > -123.0){
          if (feature[88] <= -74.0) {
           if (feature[102] <= -99.0) {
            return false;
           }
           else if (feature[102] > -99.0){
            return false;
           }
          }
          else if (feature[88] > -74.0){
           if (feature[96] <= -116.0) {
            return false;
           }
           else if (feature[96] > -116.0){
            return false;
           }
          }
         }
        }
        else if (feature[97] > -127.0){
         if (feature[92] <= -125.0) {
          if (feature[113] <= -90.0) {
           if (feature[9] <= -61.0) {
            return false;
           }
           else if (feature[9] > -61.0){
            return false;
           }
          }
          else if (feature[113] > -90.0){
           if (feature[7] <= -128.0) {
            return false;
           }
           else if (feature[7] > -128.0){
            return false;
           }
          }
         }
         else if (feature[92] > -125.0){
          if (feature[2] <= -105.0) {
           if (feature[94] <= -120.0) {
            return false;
           }
           else if (feature[94] > -120.0){
            return false;
           }
          }
          else if (feature[2] > -105.0){
           if (feature[116] <= -124.0) {
            return false;
           }
           else if (feature[116] > -124.0){
            return false;
           }
          }
         }
        }
       }
       else if (feature[105] > -123.0){
        if (feature[38] <= -125.0) {
         if (feature[34] <= -122.0) {
          if (feature[88] <= -7.0) {
           if (feature[89] <= -91.0) {
            return false;
           }
           else if (feature[89] > -91.0){
            return false;
           }
          }
          else if (feature[88] > -7.0){
           if (feature[70] <= -118.0) {
            return false;
           }
           else if (feature[70] > -118.0){
            return false;
           }
          }
         }
         else if (feature[34] > -122.0){
          if (feature[65] <= -33.0) {
           if (feature[123] <= -118.0) {
            return false;
           }
           else if (feature[123] > -118.0){
            return false;
           }
          }
          else if (feature[65] > -33.0){
           if (feature[94] <= -121.0) {
            return false;
           }
           else if (feature[94] > -121.0){
            return false;
           }
          }
         }
        }
        else if (feature[38] > -125.0){
         if (feature[62] <= -123.0) {
          if (feature[58] <= -124.0) {
           if (feature[12] <= -127.0) {
            return false;
           }
           else if (feature[12] > -127.0){
            return false;
           }
          }
          else if (feature[58] > -124.0){
           if (feature[3] <= -120.0) {
            return false;
           }
           else if (feature[3] > -120.0){
            return false;
           }
          }
         }
         else if (feature[62] > -123.0){
          if (feature[87] <= -123.0) {
           if (feature[3] <= -126.0) {
            return false;
           }
           else if (feature[3] > -126.0){
            return false;
           }
          }
          else if (feature[87] > -123.0){
           if (feature[1] <= -127.0) {
            return false;
           }
           else if (feature[1] > -127.0){
            return false;
           }
          }
         }
        }
       }
      }
     }
     else if (feature[112] > 15.0){
      if (feature[108] <= -109.0) {
       if (feature[96] <= -126.0) {
        if (feature[23] <= -47.0) {
         if (feature[99] <= -74.0) {
          if (feature[100] <= -127.0) {
           if (feature[50] <= -126.0) {
            return false;
           }
           else if (feature[50] > -126.0){
            return false;
           }
          }
          else if (feature[100] > -127.0){
           if (feature[3] <= -112.0) {
            return false;
           }
           else if (feature[3] > -112.0){
            return false;
           }
          }
         }
         else if (feature[99] > -74.0){
          if (feature[0] <= -127.0) {
           return false;
          }
          else if (feature[0] > -127.0){
           if (feature[0] <= -94.0) {
            return false;
           }
           else if (feature[0] > -94.0){
            return false;
           }
          }
         }
        }
        else if (feature[23] > -47.0){
         if (feature[104] <= -13.0) {
          if (feature[35] <= -59.0) {
           if (feature[53] <= -86.0) {
            return false;
           }
           else if (feature[53] > -86.0){
            return false;
           }
          }
          else if (feature[35] > -59.0){
           return false;
          }
         }
         else if (feature[104] > -13.0){
          if (feature[0] <= -128.0) {
           return false;
          }
          else if (feature[0] > -128.0){
           return false;
          }
         }
        }
       }
       else if (feature[96] > -126.0){
        if (feature[92] <= -93.0) {
         if (feature[61] <= -128.0) {
          if (feature[72] <= -50.0) {
           if (feature[14] <= -127.0) {
            return false;
           }
           else if (feature[14] > -127.0){
            return false;
           }
          }
          else if (feature[72] > -50.0){
           return false;
          }
         }
         else if (feature[61] > -128.0){
          if (feature[52] <= -121.0) {
           if (feature[78] <= -118.0) {
            return false;
           }
           else if (feature[78] > -118.0){
            return false;
           }
          }
          else if (feature[52] > -121.0){
           return false;
          }
         }
        }
        else if (feature[92] > -93.0){
         if (feature[14] <= -123.0) {
          if (feature[15] <= -125.0) {
           if (feature[53] <= -122.0) {
            return false;
           }
           else if (feature[53] > -122.0){
            return false;
           }
          }
          else if (feature[15] > -125.0){
           if (feature[105] <= -118.0) {
            return false;
           }
           else if (feature[105] > -118.0){
            return false;
           }
          }
         }
         else if (feature[14] > -123.0){
          if (feature[125] <= -128.0) {
           return false;
          }
          else if (feature[125] > -128.0){
           return false;
          }
         }
        }
       }
      }
      else if (feature[108] > -109.0){
       if (feature[22] <= -127.0) {
        if (feature[20] <= -125.0) {
         if (feature[75] <= -124.0) {
          if (feature[23] <= -72.0) {
           return false;
          }
          else if (feature[23] > -72.0){
           return false;
          }
         }
         else if (feature[75] > -124.0){
          if (feature[23] <= -107.0) {
           if (feature[8] <= -15.0) {
            return false;
           }
           else if (feature[8] > -15.0){
            return false;
           }
          }
          else if (feature[23] > -107.0){
           if (feature[91] <= -119.0) {
            return false;
           }
           else if (feature[91] > -119.0){
            return false;
           }
          }
         }
        }
        else if (feature[20] > -125.0){
         if (feature[51] <= -115.0) {
          if (feature[63] <= -91.0) {
           return false;
          }
          else if (feature[63] > -91.0){
           if (feature[11] <= -119.0) {
            return false;
           }
           else if (feature[11] > -119.0){
            return false;
           }
          }
         }
         else if (feature[51] > -115.0){
          if (feature[27] <= -96.0) {
           if (feature[26] <= -127.0) {
            return false;
           }
           else if (feature[26] > -127.0){
            return false;
           }
          }
          else if (feature[27] > -96.0){
           return false;
          }
         }
        }
       }
       else if (feature[22] > -127.0){
        if (feature[64] <= -124.0) {
         if (feature[105] <= -121.0) {
          if (feature[69] <= -95.0) {
           if (feature[26] <= -74.0) {
            return false;
           }
           else if (feature[26] > -74.0){
            return false;
           }
          }
          else if (feature[69] > -95.0){
           if (feature[0] <= -128.0) {
            return false;
           }
           else if (feature[0] > -128.0){
            return false;
           }
          }
         }
         else if (feature[105] > -121.0){
          if (feature[17] <= -116.0) {
           return false;
          }
          else if (feature[17] > -116.0){
           if (feature[11] <= -110.0) {
            return false;
           }
           else if (feature[11] > -110.0){
            return false;
           }
          }
         }
        }
        else if (feature[64] > -124.0){
         if (feature[59] <= -104.0) {
          if (feature[57] <= -125.0) {
           if (feature[41] <= -120.0) {
            return false;
           }
           else if (feature[41] > -120.0){
            return false;
           }
          }
          else if (feature[57] > -125.0){
           if (feature[66] <= -121.0) {
            return false;
           }
           else if (feature[66] > -121.0){
            return false;
           }
          }
         }
         else if (feature[59] > -104.0){
          if (feature[28] <= -70.0) {
           return false;
          }
          else if (feature[28] > -70.0){
           if (feature[1] <= -127.0) {
            return false;
           }
           else if (feature[1] > -127.0){
            return false;
           }
          }
         }
        }
       }
      }
     }
    }
   }
  }
  else if (feature[72] > 0.0){
   if (feature[86] <= -127.0) {
    if (feature[114] <= -128.0) {
     if (feature[85] <= -128.0) {
      if (feature[125] <= -114.0) {
       if (feature[32] <= -123.0) {
        if (feature[67] <= -128.0) {
         if (feature[15] <= -100.0) {
          if (feature[5] <= -128.0) {
           if (feature[64] <= -123.0) {
            return false;
           }
           else if (feature[64] > -123.0){
            return false;
           }
          }
          else if (feature[5] > -128.0){
           return false;
          }
         }
         else if (feature[15] > -100.0){
          return false;
         }
        }
        else if (feature[67] > -128.0){
         if (feature[21] <= -121.0) {
          if (feature[123] <= -118.0) {
           if (feature[111] <= -11.0) {
            return false;
           }
           else if (feature[111] > -11.0){
            return false;
           }
          }
          else if (feature[123] > -118.0){
           if (feature[113] <= -125.0) {
            return false;
           }
           else if (feature[113] > -125.0){
            return false;
           }
          }
         }
         else if (feature[21] > -121.0){
          return false;
         }
        }
       }
       else if (feature[32] > -123.0){
        if (feature[104] <= -116.0) {
         return false;
        }
        else if (feature[104] > -116.0){
         if (feature[63] <= -126.0) {
          if (feature[16] <= -112.0) {
           if (feature[0] <= -101.0) {
            return false;
           }
           else if (feature[0] > -101.0){
            return false;
           }
          }
          else if (feature[16] > -112.0){
           if (feature[81] <= -51.0) {
            return false;
           }
           else if (feature[81] > -51.0){
            return false;
           }
          }
         }
         else if (feature[63] > -126.0){
          if (feature[28] <= -94.0) {
           if (feature[25] <= -122.0) {
            return false;
           }
           else if (feature[25] > -122.0){
            return false;
           }
          }
          else if (feature[28] > -94.0){
           if (feature[92] <= -120.0) {
            return false;
           }
           else if (feature[92] > -120.0){
            return false;
           }
          }
         }
        }
       }
      }
      else if (feature[125] > -114.0){
       if (feature[63] <= -128.0) {
        return false;
       }
       else if (feature[63] > -128.0){
        if (feature[55] <= -83.0) {
         if (feature[39] <= -116.0) {
          if (feature[0] <= -128.0) {
           return false;
          }
          else if (feature[0] > -128.0){
           if (feature[49] <= -9.0) {
            return false;
           }
           else if (feature[49] > -9.0){
            return false;
           }
          }
         }
         else if (feature[39] > -116.0){
          return false;
         }
        }
        else if (feature[55] > -83.0){
         if (feature[91] <= -120.0) {
          if (feature[0] <= -125.0) {
           return false;
          }
          else if (feature[0] > -125.0){
           if (feature[3] <= -126.0) {
            return false;
           }
           else if (feature[3] > -126.0){
            return false;
           }
          }
         }
         else if (feature[91] > -120.0){
          return false;
         }
        }
       }
      }
     }
     else if (feature[85] > -128.0){
      if (feature[116] <= 1.0) {
       if (feature[103] <= -119.0) {
        if (feature[39] <= -121.0) {
         if (feature[62] <= -111.0) {
          if (feature[83] <= -128.0) {
           if (feature[105] <= -42.0) {
            return false;
           }
           else if (feature[105] > -42.0){
            return false;
           }
          }
          else if (feature[83] > -128.0){
           if (feature[127] <= -95.0) {
            return false;
           }
           else if (feature[127] > -95.0){
            return false;
           }
          }
         }
         else if (feature[62] > -111.0){
          if (feature[9] <= -94.0) {
           if (feature[4] <= -128.0) {
            return false;
           }
           else if (feature[4] > -128.0){
            return false;
           }
          }
          else if (feature[9] > -94.0){
           if (feature[8] <= 20.0) {
            return false;
           }
           else if (feature[8] > 20.0){
            return false;
           }
          }
         }
        }
        else if (feature[39] > -121.0){
         if (feature[90] <= -120.0) {
          if (feature[11] <= -120.0) {
           if (feature[127] <= -115.0) {
            return false;
           }
           else if (feature[127] > -115.0){
            return false;
           }
          }
          else if (feature[11] > -120.0){
           if (feature[1] <= -128.0) {
            return false;
           }
           else if (feature[1] > -128.0){
            return false;
           }
          }
         }
         else if (feature[90] > -120.0){
          if (feature[6] <= -127.0) {
           if (feature[11] <= -128.0) {
            return false;
           }
           else if (feature[11] > -128.0){
            return false;
           }
          }
          else if (feature[6] > -127.0){
           return false;
          }
         }
        }
       }
       else if (feature[103] > -119.0){
        if (feature[19] <= -120.0) {
         if (feature[58] <= -124.0) {
          if (feature[100] <= -79.0) {
           if (feature[79] <= -126.0) {
            return false;
           }
           else if (feature[79] > -126.0){
            return false;
           }
          }
          else if (feature[100] > -79.0){
           if (feature[0] <= -124.0) {
            return false;
           }
           else if (feature[0] > -124.0){
            return false;
           }
          }
         }
         else if (feature[58] > -124.0){
          if (feature[125] <= -112.0) {
           if (feature[71] <= -115.0) {
            return false;
           }
           else if (feature[71] > -115.0){
            return false;
           }
          }
          else if (feature[125] > -112.0){
           if (feature[0] <= -73.0) {
            return false;
           }
           else if (feature[0] > -73.0){
            return false;
           }
          }
         }
        }
        else if (feature[19] > -120.0){
         if (feature[74] <= -126.0) {
          if (feature[11] <= -126.0) {
           if (feature[116] <= -21.0) {
            return false;
           }
           else if (feature[116] > -21.0){
            return false;
           }
          }
          else if (feature[11] > -126.0){
           if (feature[68] <= -113.0) {
            return false;
           }
           else if (feature[68] > -113.0){
            return false;
           }
          }
         }
         else if (feature[74] > -126.0){
          if (feature[34] <= -105.0) {
           return false;
          }
          else if (feature[34] > -105.0){
           if (feature[9] <= -108.0) {
            return false;
           }
           else if (feature[9] > -108.0){
            return false;
           }
          }
         }
        }
       }
      }
      else if (feature[116] > 1.0){
       if (feature[91] <= -97.0) {
        if (feature[49] <= -126.0) {
         if (feature[64] <= -112.0) {
          if (feature[1] <= -114.0) {
           return false;
          }
          else if (feature[1] > -114.0){
           return false;
          }
         }
         else if (feature[64] > -112.0){
          if (feature[14] <= -122.0) {
           if (feature[34] <= -111.0) {
            return false;
           }
           else if (feature[34] > -111.0){
            return false;
           }
          }
          else if (feature[14] > -122.0){
           if (feature[83] <= -126.0) {
            return false;
           }
           else if (feature[83] > -126.0){
            return false;
           }
          }
         }
        }
        else if (feature[49] > -126.0){
         if (feature[111] <= -26.0) {
          if (feature[48] <= -113.0) {
           return false;
          }
          else if (feature[48] > -113.0){
           if (feature[68] <= -74.0) {
            return false;
           }
           else if (feature[68] > -74.0){
            return false;
           }
          }
         }
         else if (feature[111] > -26.0){
          if (feature[1] <= -128.0) {
           return false;
          }
          else if (feature[1] > -128.0){
           return false;
          }
         }
        }
       }
       else if (feature[91] > -97.0){
        if (feature[44] <= -121.0) {
         if (feature[30] <= -111.0) {
          return false;
         }
         else if (feature[30] > -111.0){
          if (feature[0] <= -123.0) {
           return false;
          }
          else if (feature[0] > -123.0){
           return false;
          }
         }
        }
        else if (feature[44] > -121.0){
         if (feature[1] <= -113.0) {
          if (feature[18] <= -128.0) {
           if (feature[0] <= -109.0) {
            return false;
           }
           else if (feature[0] > -109.0){
            return false;
           }
          }
          else if (feature[18] > -128.0){
           if (feature[3] <= -109.0) {
            return false;
           }
           else if (feature[3] > -109.0){
            return false;
           }
          }
         }
         else if (feature[1] > -113.0){
          if (feature[29] <= -126.0) {
           if (feature[0] <= -109.0) {
            return false;
           }
           else if (feature[0] > -109.0){
            return false;
           }
          }
          else if (feature[29] > -126.0){
           return false;
          }
         }
        }
       }
      }
     }
    }
    else if (feature[114] > -128.0){
     if (feature[52] <= 4.0) {
      if (feature[100] <= -65.0) {
       if (feature[59] <= -127.0) {
        if (feature[114] <= -120.0) {
         if (feature[88] <= -45.0) {
          if (feature[102] <= -128.0) {
           if (feature[90] <= -128.0) {
            return false;
           }
           else if (feature[90] > -128.0){
            return false;
           }
          }
          else if (feature[102] > -128.0){
           if (feature[119] <= -45.0) {
            return false;
           }
           else if (feature[119] > -45.0){
            return false;
           }
          }
         }
         else if (feature[88] > -45.0){
          if (feature[121] <= -88.0) {
           if (feature[89] <= -79.0) {
            return false;
           }
           else if (feature[89] > -79.0){
            return false;
           }
          }
          else if (feature[121] > -88.0){
           if (feature[7] <= -128.0) {
            return false;
           }
           else if (feature[7] > -128.0){
            return false;
           }
          }
         }
        }
        else if (feature[114] > -120.0){
         if (feature[87] <= -21.0) {
          if (feature[14] <= -128.0) {
           if (feature[9] <= -10.0) {
            return false;
           }
           else if (feature[9] > -10.0){
            return false;
           }
          }
          else if (feature[14] > -128.0){
           if (feature[50] <= -128.0) {
            return false;
           }
           else if (feature[50] > -128.0){
            return false;
           }
          }
         }
         else if (feature[87] > -21.0){
          return false;
         }
        }
       }
       else if (feature[59] > -127.0){
        if (feature[104] <= 13.0) {
         if (feature[92] <= -120.0) {
          if (feature[23] <= 3.0) {
           if (feature[50] <= -52.0) {
            return false;
           }
           else if (feature[50] > -52.0){
            return false;
           }
          }
          else if (feature[23] > 3.0){
           if (feature[52] <= -113.0) {
            return false;
           }
           else if (feature[52] > -113.0){
            return false;
           }
          }
         }
         else if (feature[92] > -120.0){
          if (feature[32] <= -126.0) {
           if (feature[35] <= -128.0) {
            return false;
           }
           else if (feature[35] > -128.0){
            return false;
           }
          }
          else if (feature[32] > -126.0){
           if (feature[73] <= -8.0) {
            return false;
           }
           else if (feature[73] > -8.0){
            return false;
           }
          }
         }
        }
        else if (feature[104] > 13.0){
         if (feature[93] <= -128.0) {
          if (feature[108] <= -126.0) {
           if (feature[50] <= -112.0) {
            return false;
           }
           else if (feature[50] > -112.0){
            return false;
           }
          }
          else if (feature[108] > -126.0){
           if (feature[119] <= -125.0) {
            return false;
           }
           else if (feature[119] > -125.0){
            return false;
           }
          }
         }
         else if (feature[93] > -128.0){
          if (feature[27] <= -128.0) {
           if (feature[115] <= -99.0) {
            return false;
           }
           else if (feature[115] > -99.0){
            return false;
           }
          }
          else if (feature[27] > -128.0){
           if (feature[18] <= -122.0) {
            return false;
           }
           else if (feature[18] > -122.0){
            return false;
           }
          }
         }
        }
       }
      }
      else if (feature[100] > -65.0){
       if (feature[8] <= -72.0) {
        if (feature[117] <= -121.0) {
         if (feature[72] <= 8.0) {
          if (feature[34] <= -91.0) {
           return false;
          }
          else if (feature[34] > -91.0){
           return false;
          }
         }
         else if (feature[72] > 8.0){
          if (feature[3] <= -128.0) {
           return false;
          }
          else if (feature[3] > -128.0){
           if (feature[2] <= -127.0) {
            return false;
           }
           else if (feature[2] > -127.0){
            return false;
           }
          }
         }
        }
        else if (feature[117] > -121.0){
         if (feature[92] <= -75.0) {
          if (feature[78] <= -128.0) {
           return false;
          }
          else if (feature[78] > -128.0){
           if (feature[2] <= -98.0) {
            return false;
           }
           else if (feature[2] > -98.0){
            return false;
           }
          }
         }
         else if (feature[92] > -75.0){
          return false;
         }
        }
       }
       else if (feature[8] > -72.0){
        if (feature[81] <= -79.0) {
         if (feature[16] <= -27.0) {
          if (feature[41] <= -88.0) {
           if (feature[6] <= -125.0) {
            return false;
           }
           else if (feature[6] > -125.0){
            return false;
           }
          }
          else if (feature[41] > -88.0){
           if (feature[0] <= -121.0) {
            return false;
           }
           else if (feature[0] > -121.0){
            return false;
           }
          }
         }
         else if (feature[16] > -27.0){
          if (feature[30] <= -128.0) {
           return false;
          }
          else if (feature[30] > -128.0){
           if (feature[27] <= -113.0) {
            return false;
           }
           else if (feature[27] > -113.0){
            return false;
           }
          }
         }
        }
        else if (feature[81] > -79.0){
         if (feature[74] <= -110.0) {
          if (feature[18] <= -111.0) {
           return false;
          }
          else if (feature[18] > -111.0){
           return false;
          }
         }
         else if (feature[74] > -110.0){
          return false;
         }
        }
       }
      }
     }
     else if (feature[52] > 4.0){
      if (feature[47] <= -17.0) {
       if (feature[87] <= -125.0) {
        if (feature[94] <= -128.0) {
         if (feature[79] <= -103.0) {
          if (feature[43] <= -121.0) {
           if (feature[87] <= -127.0) {
            return false;
           }
           else if (feature[87] > -127.0){
            return false;
           }
          }
          else if (feature[43] > -121.0){
           if (feature[10] <= -122.0) {
            return false;
           }
           else if (feature[10] > -122.0){
            return false;
           }
          }
         }
         else if (feature[79] > -103.0){
          if (feature[23] <= -112.0) {
           if (feature[124] <= -91.0) {
            return false;
           }
           else if (feature[124] > -91.0){
            return false;
           }
          }
          else if (feature[23] > -112.0){
           if (feature[20] <= -20.0) {
            return false;
           }
           else if (feature[20] > -20.0){
            return false;
           }
          }
         }
        }
        else if (feature[94] > -128.0){
         if (feature[123] <= -101.0) {
          if (feature[7] <= -127.0) {
           if (feature[38] <= -128.0) {
            return false;
           }
           else if (feature[38] > -128.0){
            return false;
           }
          }
          else if (feature[7] > -127.0){
           if (feature[120] <= -84.0) {
            return false;
           }
           else if (feature[120] > -84.0){
            return false;
           }
          }
         }
         else if (feature[123] > -101.0){
          if (feature[21] <= -88.0) {
           if (feature[7] <= -98.0) {
            return false;
           }
           else if (feature[7] > -98.0){
            return false;
           }
          }
          else if (feature[21] > -88.0){
           return false;
          }
         }
        }
       }
       else if (feature[87] > -125.0){
        if (feature[0] <= -98.0) {
         if (feature[12] <= -111.0) {
          if (feature[81] <= -104.0) {
           if (feature[59] <= -86.0) {
            return false;
           }
           else if (feature[59] > -86.0){
            return false;
           }
          }
          else if (feature[81] > -104.0){
           return false;
          }
         }
         else if (feature[12] > -111.0){
          if (feature[93] <= -114.0) {
           return false;
          }
          else if (feature[93] > -114.0){
           if (feature[1] <= -128.0) {
            return false;
           }
           else if (feature[1] > -128.0){
            return false;
           }
          }
         }
        }
        else if (feature[0] > -98.0){
         if (feature[88] <= -108.0) {
          if (feature[45] <= -124.0) {
           if (feature[53] <= -123.0) {
            return false;
           }
           else if (feature[53] > -123.0){
            return false;
           }
          }
          else if (feature[45] > -124.0){
           if (feature[71] <= -101.0) {
            return false;
           }
           else if (feature[71] > -101.0){
            return false;
           }
          }
         }
         else if (feature[88] > -108.0){
          if (feature[122] <= -117.0) {
           if (feature[1] <= -88.0) {
            return false;
           }
           else if (feature[1] > -88.0){
            return false;
           }
          }
          else if (feature[122] > -117.0){
           if (feature[6] <= -128.0) {
            return false;
           }
           else if (feature[6] > -128.0){
            return false;
           }
          }
         }
        }
       }
      }
      else if (feature[47] > -17.0){
       if (feature[108] <= -112.0) {
        if (feature[8] <= -7.0) {
         if (feature[12] <= -94.0) {
          if (feature[11] <= -77.0) {
           if (feature[81] <= -51.0) {
            return false;
           }
           else if (feature[81] > -51.0){
            return false;
           }
          }
          else if (feature[11] > -77.0){
           return false;
          }
         }
         else if (feature[12] > -94.0){
          return false;
         }
        }
        else if (feature[8] > -7.0){
         if (feature[37] <= -128.0) {
          if (feature[0] <= -85.0) {
           return false;
          }
          else if (feature[0] > -85.0){
           return false;
          }
         }
         else if (feature[37] > -128.0){
          if (feature[73] <= -8.0) {
           return false;
          }
          else if (feature[73] > -8.0){
           return false;
          }
         }
        }
       }
       else if (feature[108] > -112.0){
        if (feature[89] <= -125.0) {
         if (feature[67] <= -127.0) {
          if (feature[0] <= -118.0) {
           return false;
          }
          else if (feature[0] > -118.0){
           return false;
          }
         }
         else if (feature[67] > -127.0){
          if (feature[7] <= -114.0) {
           return false;
          }
          else if (feature[7] > -114.0){
           return false;
          }
         }
        }
        else if (feature[89] > -125.0){
         if (feature[110] <= -107.0) {
          if (feature[1] <= -128.0) {
           return false;
          }
          else if (feature[1] > -128.0){
           return false;
          }
         }
         else if (feature[110] > -107.0){
          return false;
         }
        }
       }
      }
     }
    }
   }
   else if (feature[86] > -127.0){
    if (feature[84] <= -36.0) {
     if (feature[27] <= -127.0) {
      if (feature[60] <= -96.0) {
       if (feature[47] <= 0.0) {
        if (feature[48] <= -5.0) {
         if (feature[83] <= -48.0) {
          if (feature[110] <= -127.0) {
           if (feature[111] <= -102.0) {
            return false;
           }
           else if (feature[111] > -102.0){
            return false;
           }
          }
          else if (feature[110] > -127.0){
           if (feature[50] <= -114.0) {
            return false;
           }
           else if (feature[50] > -114.0){
            return false;
           }
          }
         }
         else if (feature[83] > -48.0){
          if (feature[105] <= -116.0) {
           if (feature[93] <= -119.0) {
            return false;
           }
           else if (feature[93] > -119.0){
            return false;
           }
          }
          else if (feature[105] > -116.0){
           if (feature[29] <= -80.0) {
            return false;
           }
           else if (feature[29] > -80.0){
            return false;
           }
          }
         }
        }
        else if (feature[48] > -5.0){
         if (feature[65] <= -115.0) {
          if (feature[29] <= -104.0) {
           if (feature[31] <= -102.0) {
            return false;
           }
           else if (feature[31] > -102.0){
            return false;
           }
          }
          else if (feature[29] > -104.0){
           if (feature[113] <= -116.0) {
            return false;
           }
           else if (feature[113] > -116.0){
            return false;
           }
          }
         }
         else if (feature[65] > -115.0){
          if (feature[91] <= -118.0) {
           return false;
          }
          else if (feature[91] > -118.0){
           if (feature[4] <= -126.0) {
            return false;
           }
           else if (feature[4] > -126.0){
            return false;
           }
          }
         }
        }
       }
       else if (feature[47] > 0.0){
        if (feature[61] <= -93.0) {
         if (feature[8] <= -88.0) {
          if (feature[67] <= -125.0) {
           if (feature[4] <= -128.0) {
            return false;
           }
           else if (feature[4] > -128.0){
            return false;
           }
          }
          else if (feature[67] > -125.0){
           if (feature[71] <= -120.0) {
            return false;
           }
           else if (feature[71] > -120.0){
            return false;
           }
          }
         }
         else if (feature[8] > -88.0){
          if (feature[79] <= -114.0) {
           return false;
          }
          else if (feature[79] > -114.0){
           if (feature[87] <= -122.0) {
            return false;
           }
           else if (feature[87] > -122.0){
            return false;
           }
          }
         }
        }
        else if (feature[61] > -93.0){
         if (feature[9] <= -94.0) {
          if (feature[119] <= -111.0) {
           if (feature[121] <= -128.0) {
            return false;
           }
           else if (feature[121] > -128.0){
            return false;
           }
          }
          else if (feature[119] > -111.0){
           if (feature[119] <= -100.0) {
            return false;
           }
           else if (feature[119] > -100.0){
            return false;
           }
          }
         }
         else if (feature[9] > -94.0){
          if (feature[9] <= -39.0) {
           return false;
          }
          else if (feature[9] > -39.0){
           if (feature[0] <= -122.0) {
            return false;
           }
           else if (feature[0] > -122.0){
            return false;
           }
          }
         }
        }
       }
      }
      else if (feature[60] > -96.0){
       if (feature[125] <= -58.0) {
        if (feature[68] <= -83.0) {
         if (feature[30] <= -124.0) {
          if (feature[0] <= -126.0) {
           if (feature[9] <= -128.0) {
            return false;
           }
           else if (feature[9] > -128.0){
            return false;
           }
          }
          else if (feature[0] > -126.0){
           if (feature[122] <= -99.0) {
            return false;
           }
           else if (feature[122] > -99.0){
            return false;
           }
          }
         }
         else if (feature[30] > -124.0){
          if (feature[30] <= -115.0) {
           if (feature[104] <= 22.0) {
            return false;
           }
           else if (feature[104] > 22.0){
            return false;
           }
          }
          else if (feature[30] > -115.0){
           if (feature[13] <= -128.0) {
            return false;
           }
           else if (feature[13] > -128.0){
            return false;
           }
          }
         }
        }
        else if (feature[68] > -83.0){
         if (feature[80] <= -23.0) {
          if (feature[65] <= -121.0) {
           if (feature[28] <= -127.0) {
            return false;
           }
           else if (feature[28] > -127.0){
            return false;
           }
          }
          else if (feature[65] > -121.0){
           if (feature[2] <= -113.0) {
            return false;
           }
           else if (feature[2] > -113.0){
            return false;
           }
          }
         }
         else if (feature[80] > -23.0){
          if (feature[9] <= -61.0) {
           return false;
          }
          else if (feature[9] > -61.0){
           if (feature[0] <= -109.0) {
            return false;
           }
           else if (feature[0] > -109.0){
            return false;
           }
          }
         }
        }
       }
       else if (feature[125] > -58.0){
        if (feature[121] <= -109.0) {
         if (feature[19] <= -122.0) {
          if (feature[39] <= -128.0) {
           return false;
          }
          else if (feature[39] > -128.0){
           return false;
          }
         }
         else if (feature[19] > -122.0){
          return false;
         }
        }
        else if (feature[121] > -109.0){
         return false;
        }
       }
      }
     }
     else if (feature[27] > -127.0){
      if (feature[76] <= -69.0) {
       if (feature[22] <= -120.0) {
        if (feature[60] <= -88.0) {
         if (feature[32] <= -123.0) {
          if (feature[1] <= -128.0) {
           if (feature[15] <= -97.0) {
            return false;
           }
           else if (feature[15] > -97.0){
            return false;
           }
          }
          else if (feature[1] > -128.0){
           if (feature[7] <= -123.0) {
            return false;
           }
           else if (feature[7] > -123.0){
            return false;
           }
          }
         }
         else if (feature[32] > -123.0){
          if (feature[23] <= -114.0) {
           if (feature[16] <= -109.0) {
            return false;
           }
           else if (feature[16] > -109.0){
            return false;
           }
          }
          else if (feature[23] > -114.0){
           if (feature[106] <= -122.0) {
            return false;
           }
           else if (feature[106] > -122.0){
            return false;
           }
          }
         }
        }
        else if (feature[60] > -88.0){
         if (feature[104] <= 13.0) {
          if (feature[55] <= -115.0) {
           if (feature[103] <= -127.0) {
            return false;
           }
           else if (feature[103] > -127.0){
            return false;
           }
          }
          else if (feature[55] > -115.0){
           if (feature[81] <= -124.0) {
            return false;
           }
           else if (feature[81] > -124.0){
            return false;
           }
          }
         }
         else if (feature[104] > 13.0){
          if (feature[124] <= -103.0) {
           if (feature[127] <= -127.0) {
            return false;
           }
           else if (feature[127] > -127.0){
            return false;
           }
          }
          else if (feature[124] > -103.0){
           if (feature[90] <= -104.0) {
            return false;
           }
           else if (feature[90] > -104.0){
            return false;
           }
          }
         }
        }
       }
       else if (feature[22] > -120.0){
        if (feature[122] <= -123.0) {
         if (feature[125] <= -119.0) {
          if (feature[114] <= -104.0) {
           if (feature[84] <= -49.0) {
            return false;
           }
           else if (feature[84] > -49.0){
            return false;
           }
          }
          else if (feature[114] > -104.0){
           if (feature[31] <= -128.0) {
            return false;
           }
           else if (feature[31] > -128.0){
            return false;
           }
          }
         }
         else if (feature[125] > -119.0){
          if (feature[118] <= -125.0) {
           if (feature[16] <= -115.0) {
            return false;
           }
           else if (feature[16] > -115.0){
            return false;
           }
          }
          else if (feature[118] > -125.0){
           if (feature[24] <= -110.0) {
            return false;
           }
           else if (feature[24] > -110.0){
            return false;
           }
          }
         }
        }
        else if (feature[122] > -123.0){
         if (feature[33] <= -126.0) {
          if (feature[38] <= -128.0) {
           if (feature[41] <= -118.0) {
            return false;
           }
           else if (feature[41] > -118.0){
            return false;
           }
          }
          else if (feature[38] > -128.0){
           if (feature[111] <= -125.0) {
            return false;
           }
           else if (feature[111] > -125.0){
            return false;
           }
          }
         }
         else if (feature[33] > -126.0){
          if (feature[98] <= -124.0) {
           if (feature[80] <= -107.0) {
            return false;
           }
           else if (feature[80] > -107.0){
            return false;
           }
          }
          else if (feature[98] > -124.0){
           if (feature[28] <= -127.0) {
            return false;
           }
           else if (feature[28] > -127.0){
            return false;
           }
          }
         }
        }
       }
      }
      else if (feature[76] > -69.0){
       if (feature[12] <= -99.0) {
        if (feature[51] <= -120.0) {
         return false;
        }
        else if (feature[51] > -120.0){
         return false;
        }
       }
       else if (feature[12] > -99.0){
        if (feature[0] <= -38.0) {
         return false;
        }
        else if (feature[0] > -38.0){
         return false;
        }
       }
      }
     }
    }
    else if (feature[84] > -36.0){
     if (feature[72] <= 37.0) {
      if (feature[47] <= 0.0) {
       if (feature[7] <= -123.0) {
        if (feature[49] <= -126.0) {
         if (feature[100] <= -112.0) {
          if (feature[10] <= -127.0) {
           if (feature[35] <= -96.0) {
            return false;
           }
           else if (feature[35] > -96.0){
            return false;
           }
          }
          else if (feature[10] > -127.0){
           if (feature[33] <= -82.0) {
            return false;
           }
           else if (feature[33] > -82.0){
            return false;
           }
          }
         }
         else if (feature[100] > -112.0){
          if (feature[76] <= -69.0) {
           if (feature[61] <= -112.0) {
            return false;
           }
           else if (feature[61] > -112.0){
            return false;
           }
          }
          else if (feature[76] > -69.0){
           return false;
          }
         }
        }
        else if (feature[49] > -126.0){
         if (feature[26] <= -128.0) {
          if (feature[28] <= -35.0) {
           if (feature[41] <= -63.0) {
            return false;
           }
           else if (feature[41] > -63.0){
            return false;
           }
          }
          else if (feature[28] > -35.0){
           if (feature[53] <= -115.0) {
            return false;
           }
           else if (feature[53] > -115.0){
            return false;
           }
          }
         }
         else if (feature[26] > -128.0){
          if (feature[124] <= -79.0) {
           if (feature[79] <= -115.0) {
            return false;
           }
           else if (feature[79] > -115.0){
            return false;
           }
          }
          else if (feature[124] > -79.0){
           if (feature[46] <= -112.0) {
            return false;
           }
           else if (feature[46] > -112.0){
            return false;
           }
          }
         }
        }
       }
       else if (feature[7] > -123.0){
        if (feature[29] <= -118.0) {
         if (feature[126] <= -120.0) {
          if (feature[76] <= -111.0) {
           if (feature[25] <= -114.0) {
            return false;
           }
           else if (feature[25] > -114.0){
            return false;
           }
          }
          else if (feature[76] > -111.0){
           if (feature[110] <= -125.0) {
            return false;
           }
           else if (feature[110] > -125.0){
            return false;
           }
          }
         }
         else if (feature[126] > -120.0){
          if (feature[49] <= -123.0) {
           if (feature[51] <= -127.0) {
            return false;
           }
           else if (feature[51] > -127.0){
            return false;
           }
          }
          else if (feature[49] > -123.0){
           if (feature[35] <= -116.0) {
            return false;
           }
           else if (feature[35] > -116.0){
            return false;
           }
          }
         }
        }
        else if (feature[29] > -118.0){
         if (feature[123] <= -125.0) {
          if (feature[111] <= -108.0) {
           if (feature[45] <= -117.0) {
            return false;
           }
           else if (feature[45] > -117.0){
            return false;
           }
          }
          else if (feature[111] > -108.0){
           if (feature[60] <= -96.0) {
            return false;
           }
           else if (feature[60] > -96.0){
            return false;
           }
          }
         }
         else if (feature[123] > -125.0){
          if (feature[90] <= -125.0) {
           if (feature[71] <= -119.0) {
            return false;
           }
           else if (feature[71] > -119.0){
            return false;
           }
          }
          else if (feature[90] > -125.0){
           if (feature[83] <= -34.0) {
            return false;
           }
           else if (feature[83] > -34.0){
            return false;
           }
          }
         }
        }
       }
      }
      else if (feature[47] > 0.0){
       if (feature[0] <= -114.0) {
        if (feature[104] <= -22.0) {
         if (feature[37] <= -91.0) {
          if (feature[42] <= -117.0) {
           if (feature[105] <= -122.0) {
            return false;
           }
           else if (feature[105] > -122.0){
            return false;
           }
          }
          else if (feature[42] > -117.0){
           return false;
          }
         }
         else if (feature[37] > -91.0){
          if (feature[65] <= -122.0) {
           if (feature[7] <= -122.0) {
            return false;
           }
           else if (feature[7] > -122.0){
            return false;
           }
          }
          else if (feature[65] > -122.0){
           if (feature[13] <= -128.0) {
            return false;
           }
           else if (feature[13] > -128.0){
            return false;
           }
          }
         }
        }
        else if (feature[104] > -22.0){
         if (feature[66] <= -125.0) {
          if (feature[51] <= -128.0) {
           if (feature[106] <= -126.0) {
            return false;
           }
           else if (feature[106] > -126.0){
            return false;
           }
          }
          else if (feature[51] > -128.0){
           if (feature[110] <= -124.0) {
            return false;
           }
           else if (feature[110] > -124.0){
            return false;
           }
          }
         }
         else if (feature[66] > -125.0){
          if (feature[43] <= -121.0) {
           if (feature[6] <= -112.0) {
            return false;
           }
           else if (feature[6] > -112.0){
            return false;
           }
          }
          else if (feature[43] > -121.0){
           if (feature[1] <= -128.0) {
            return false;
           }
           else if (feature[1] > -128.0){
            return false;
           }
          }
         }
        }
       }
       else if (feature[0] > -114.0){
        if (feature[44] <= -109.0) {
         if (feature[48] <= -103.0) {
          if (feature[10] <= -112.0) {
           if (feature[71] <= -127.0) {
            return false;
           }
           else if (feature[71] > -127.0){
            return false;
           }
          }
          else if (feature[10] > -112.0){
           if (feature[26] <= -114.0) {
            return false;
           }
           else if (feature[26] > -114.0){
            return false;
           }
          }
         }
         else if (feature[48] > -103.0){
          if (feature[74] <= -108.0) {
           if (feature[19] <= -97.0) {
            return false;
           }
           else if (feature[19] > -97.0){
            return false;
           }
          }
          else if (feature[74] > -108.0){
           if (feature[15] <= -103.0) {
            return false;
           }
           else if (feature[15] > -103.0){
            return false;
           }
          }
         }
        }
        else if (feature[44] > -109.0){
         if (feature[103] <= -110.0) {
          return false;
         }
         else if (feature[103] > -110.0){
          if (feature[11] <= -128.0) {
           return false;
          }
          else if (feature[11] > -128.0){
           return false;
          }
         }
        }
       }
      }
     }
     else if (feature[72] > 37.0){
      if (feature[93] <= -96.0) {
       if (feature[102] <= -128.0) {
        return false;
       }
       else if (feature[102] > -128.0){
        if (feature[0] <= -101.0) {
         return false;
        }
        else if (feature[0] > -101.0){
         return false;
        }
       }
      }
      else if (feature[93] > -96.0){
       if (feature[14] <= -127.0) {
        return false;
       }
       else if (feature[14] > -127.0){
        return false;
       }
      }
     }
    }
   }
  }
    return false;
    }
}
