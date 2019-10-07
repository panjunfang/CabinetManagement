package com.policeequipment.android.cabinetmanagement.util;

/**
 * authorï¼ deemons
 * date:    2018/5/26
 * desc:
 */
public interface SPKey {

    String SU_PATH      = "su_path";
    String SERIAL_PORT  = "serial_port";
    String BAUD_RATE    = "baud_rate";
    String DATA_BITS    = "data_bits";
    String CHECK_DIGIT  = "check_digit";
    String STOP_BIT     = "stop_bit";
    String FLOW_CONTROL = "flow_control";

    String SERIAL_PORT_IC  = "serial_port_ic";
    String BAUD_RATE_IC    = "baud_rate_ic";
    String DATA_BITS_IC    = "data_bits_ic";
    String CHECK_DIGIT_IC  = "check_digit_ic";
    String STOP_BIT_IC     = "stop_bit_ic";

    String SERIAL_PORT_X86  = "serial_port_x86";
    String BAUD_RATE_X86    = "baud_rate_x86";
    String DATA_BITS_X86    = "data_bits_x86";
    String CHECK_DIGIT_X86  = "check_digit_x86";
    String STOP_BIT_X86     = "stop_bit_x86";

    String BoardNumber1 = "BoardNumber1";
    String BoardNumber2 = "BoardNumber2";


    String SETTING_RECEIVE_TYPE = "receive_type";
    String SETTING_RECEIVE_SHOW_TIME = "setting_receive_show_time";
    String SETTING_RECEIVE_SHOW_SEND = "setting_receive_show_send";

    String SETTING_SEND_TYPE   = "setting_send_type";
    String SETTING_SEND_REPEAT = "setting_send_repeat";
    String SETTING_SEND_DURING = "setting_send_during";
    String SEND_HISTORY        = "send_history";

    String Gate1 = "060500110001011408";
    String Gate2 = "060500110001021708";
    String Gate3 = "060500110001031608";
    String Gate4 = "060500110001041108";
    String Gate5 =      "060500110001051008";
    String Gate6 =      "060500110001061308";
    String Gate7=       "060500110001071208";
    String Gate8 =      "060500110001081D08";
    String OpenAll_1 =    "060500110001001508";


    String Gate9 =      "060500110002091C08";
    String Gate10 =     "060500110001100508";
    String Gate11 =     "060500110001110408";
    String Gate12 =     "060500110001120708";

    String Gate13 =     "060500110001130608";
    String Gate14 =     "060500110001140108";
    String Gate15 =     "060500110001150008";
    String Gate16 =     "060500110001160308";


    String OpenAll_2 =    "060500110002001608";

    String QueryLock1 =  "060501110001011508";
    String QueryLock2 = "060501110002011608";




    String[] InstructionSet = new String[]{
            "060500110001011408",
            "060500110001021708",
            "060500110001031608",
            "060500110001041108",
            "060500110001051008",
            "060500110001061308",
            "060500110001071208",
            "060500110001081D08",
            "060500110002011708",
            "060500110002021408",
            "060500110002031508",
            "060500110002041208",
            "060500110002051308",
            "060500110002061008",
            "060500110002071108",
            "060500110002081E08"
    };





    /**
     * ic
     */

    String Query_card_number = "0108A12000010076";//查询卡号
      String Administrator_password = "1";




}
