package com.sql_processor;

import javax.management.AttributeNotFoundException;
import java.util.Arrays;
import java.util.Map;

public class Update {
//    public static Map<Long, Map<String, Object>> update(String[] tokens, Map<Long, Map<String, Object>> data) throws Exception {
//        if (!tokens[1].equalsIgnoreCase("set")) {
//            throw new Exception("Invalid command syntax: " + String.join(" ", tokens[1]));
//        }
//        int whereCondition = 0;
//        for (int i=0; i<=tokens.length-1; i++){
//            if (tokens[i].equalsIgnoreCase("where")) {
//                whereCondition = i;
//                break;
//            }
//        }
//        if(whereCondition == 0) throw new AttributeNotFoundException("where condition must be present");
//
//        String[] values = String.join(" ", Arrays.copyOfRange(tokens, 2, whereCondition)).split(",");
//        String[] keys = Arrays.copyOfRange(tokens, whereCondition + 1, tokens.length);
//        Map<Long, Map<String, Object>> whereList = Where.where(keys, data);
//        for (String s : values) {
//            String sub;
//            String value;
//            int firstQuoteIndex = s.indexOf("'") + 1;
//            int secondQuoteIndex = s.indexOf("'", firstQuoteIndex + 1);
//            sub = s.substring(firstQuoteIndex, secondQuoteIndex);
//            if (sub.replace(" ", "").equalsIgnoreCase("lastName")) {
//                int thirdQuoteIndex = s.indexOf("'", secondQuoteIndex + 1);
//                int fourthQuoteIndex = s.lastIndexOf("'");
//                value = thirdQuoteIndex > 1 & fourthQuoteIndex > 1 ? s.substring(thirdQuoteIndex + 1, fourthQuoteIndex) : "";
//            } else {
//                value = s.substring(s.indexOf("=")+1).trim();
//            }
//            switch (sub.toLowerCase().replace(" ", "")) {
//                case "lastname":
//                    if (!value.isEmpty()) {
//                        if (!value.equalsIgnoreCase("null")) {
//                            data.forEach((k, v) -> {
//                                whereList.forEach((wk, vv) -> {
//                                    if (v.keySet().containsAll(vv.keySet()) && v.entrySet().containsAll(vv.entrySet())) {
//                                        v.replace("lastName", (value.matches("^[a-zA-Z ]*$") | value.matches("^[а-яА-Я ]*$")) ? value: null);
//                                    }
//                                });
//                            });
//                        } else {
//                            data.forEach((k, v) -> {
//                                whereList.forEach((wk, vv) -> {
//                                    if (v.keySet().containsAll(vv.keySet()) && v.entrySet().containsAll(vv.entrySet())) {
//                                        v.replace("lastName", null);
//                                    }
//                                });
//                            });
//                        }
//                    }
//                    break;
//                case "age":
//                    if (!value.isEmpty()) {
//                        if (!value.equalsIgnoreCase("null")) {
//                            data.forEach((k, v) -> {
//                                whereList.forEach((wk, vv) -> {
//                                    if (v.keySet().containsAll(vv.keySet()) && v.entrySet().containsAll(vv.entrySet())) {
//                                        v.replace("age", Short.parseShort(value));
//                                        vv.replace("age", Short.parseShort(value));
//                                    }
//                                });
//                            });
//                        } else {
//                            data.forEach((k, v) -> {
//                                whereList.forEach((wk, vv) -> {
//                                    if (v.keySet().containsAll(vv.keySet()) && v.entrySet().containsAll(vv.entrySet())) {
//                                        v.replace("age", null);
//                                        vv.replace("age", null);
//                                    }
//                                });
//                            });
//                        }
//                    }
//                    break;
//                case "cost":
//                    if (!value.isEmpty()) {
//                        if (!value.equalsIgnoreCase("null")) {
//                            data.forEach((k, v) -> {
//                                whereList.forEach((wk, vv) -> {
//                                    if (v.keySet().containsAll(vv.keySet()) && v.entrySet().containsAll(vv.entrySet())) {
//                                        v.replace("cost", Double.parseDouble(value));
//                                        vv.replace("cost", Double.parseDouble(value));
//                                    }
//                                });
//                            });
//                        } else {
//                            data.forEach((k, v) -> {
//                                whereList.forEach((wk, vv) -> {
//                                    if (v.keySet().containsAll(vv.keySet()) && v.entrySet().containsAll(vv.entrySet())) {
//                                        v.replace("cost", null);
//                                        vv.replace("cost", null);
//                                    }
//                                });
//                            });
//                        }
//                    }
//                    break;
//                case "active":
//                    if (!value.isEmpty()) {
//                        if (!value.equalsIgnoreCase("null")) {
//                            data.forEach((k, v) -> {
//                                whereList.forEach((wk, vv) -> {
//                                    if (v.keySet().containsAll(vv.keySet()) && v.entrySet().containsAll(vv.entrySet())) {
//                                        v.replace("active", Boolean.parseBoolean(value));
//                                        vv.replace("active", Boolean.parseBoolean(value));
//                                    }
//                                });
//                            });
//                        } else {
//                            data.forEach((k, v) -> {
//                                whereList.forEach((wk, vv) -> {
//                                    if (v.keySet().containsAll(vv.keySet()) && v.entrySet().containsAll(vv.entrySet())) {
//                                        v.replace("active", null);
//                                        vv.replace("active", null);
//                                    }
//                                });
//                            });
//                        }
//                    }
//                    break;
//                case "id":
//                    break;
//            }
//        }
//        return data;
//    }
}
