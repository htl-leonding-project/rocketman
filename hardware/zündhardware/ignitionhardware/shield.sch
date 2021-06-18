EESchema Schematic File Version 4
EELAYER 30 0
EELAYER END
$Descr A4 11693 8268
encoding utf-8
Sheet 2 2
Title ""
Date ""
Rev ""
Comp ""
Comment1 ""
Comment2 ""
Comment3 ""
Comment4 ""
$EndDescr
$Comp
L Connector_Generic:Conn_02x20_Odd_Even J?
U 1 1 60BBA854
P 7200 3400
F 0 "J?" H 7250 4517 50  0000 C CNN
F 1 "Conn_02x20_Odd_Even" H 7250 4426 50  0000 C CNN
F 2 "" H 7200 3400 50  0001 C CNN
F 3 "~" H 7200 3400 50  0001 C CNN
	1    7200 3400
	1    0    0    -1  
$EndComp
Connection ~ 4300 3300
Wire Wire Line
	4300 3300 4300 3250
Wire Wire Line
	4300 3300 4000 3300
Wire Wire Line
	4900 3700 4900 4000
Wire Wire Line
	4700 3700 4900 3700
Wire Wire Line
	4850 3800 4850 4100
Wire Wire Line
	4700 3800 4850 3800
Wire Wire Line
	4800 3900 4800 4200
Wire Wire Line
	4700 3900 4800 3900
Wire Wire Line
	4750 4000 4750 4300
Wire Wire Line
	4700 4000 4750 4000
Connection ~ 4300 4400
Wire Wire Line
	4000 4400 4300 4400
$Comp
L Analog_ADC:MCP3008 U?
U 1 1 60BC128E
P 4100 3800
F 0 "U?" H 4100 4481 50  0000 C CNN
F 1 "MCP3008" H 4100 4390 50  0000 C CNN
F 2 "" H 4200 3900 50  0001 C CNN
F 3 "http://ww1.microchip.com/downloads/en/DeviceDoc/21295d.pdf" H 4200 3900 50  0001 C CNN
	1    4100 3800
	1    0    0    -1  
$EndComp
Wire Wire Line
	4300 2500 6700 2500
Wire Wire Line
	4900 4000 7000 4000
Wire Wire Line
	4850 4100 7000 4100
Wire Wire Line
	4800 4200 7000 4200
Wire Wire Line
	4750 4300 7000 4300
Wire Wire Line
	4300 4400 4550 4400
$Comp
L Connector:Conn_01x05_Female Joystick
U 1 1 60BD9B81
P 2750 3500
F 0 "Joystick" H 2642 3885 50  0000 C CNN
F 1 "Conn_01x05_Female" H 2642 3794 50  0000 C CNN
F 2 "" H 2750 3500 50  0001 C CNN
F 3 "~" H 2750 3500 50  0001 C CNN
	1    2750 3500
	-1   0    0    -1  
$EndComp
Wire Wire Line
	2950 3500 3500 3500
Wire Wire Line
	2950 3600 3500 3600
Wire Wire Line
	2950 3700 3500 3700
Wire Wire Line
	2950 3400 3450 3400
Wire Wire Line
	3450 3250 4300 3250
Wire Wire Line
	3450 3250 3450 3400
Connection ~ 4300 3250
Wire Wire Line
	4300 3250 4300 2500
Wire Wire Line
	2950 3300 2950 3000
Wire Wire Line
	2950 3000 2400 3000
Wire Wire Line
	2400 3000 2400 4500
Wire Wire Line
	2400 4500 4550 4500
Wire Wire Line
	4550 4500 4550 4400
Connection ~ 4550 4400
Wire Wire Line
	4550 4400 7000 4400
Wire Wire Line
	7500 3100 7700 3100
Wire Wire Line
	7700 3100 7700 3000
$Comp
L power:+3.3V #PWR?
U 1 1 60BE5F24
P 6450 3000
AR Path="/60BE5F24" Ref="#PWR?"  Part="1" 
AR Path="/60BA82D9/60BE5F24" Ref="#PWR?"  Part="1" 
F 0 "#PWR?" H 6450 2850 50  0001 C CNN
F 1 "+3.3V" H 6465 3173 50  0000 C CNN
F 2 "" H 6450 3000 50  0001 C CNN
F 3 "" H 6450 3000 50  0001 C CNN
	1    6450 3000
	-1   0    0    -1  
$EndComp
$Comp
L Connector:Conn_01x02_Female Zünbutton
U 1 1 60BE81B9
P 5600 3000
F 0 "Zünbutton" H 5492 3185 50  0000 C CNN
F 1 "Conn_01x02_Female" H 5492 3094 50  0000 C CNN
F 2 "" H 5600 3000 50  0001 C CNN
F 3 "~" H 5600 3000 50  0001 C CNN
	1    5600 3000
	-1   0    0    -1  
$EndComp
$Comp
L Device:R R?
U 1 1 60BE5F2F
P 6300 3000
AR Path="/60BE5F2F" Ref="R?"  Part="1" 
AR Path="/60BA82D9/60BE5F2F" Ref="R?"  Part="1" 
F 0 "R?" V 6093 3000 50  0000 C CNN
F 1 "10" V 6184 3000 50  0000 C CNN
F 2 "" V 6230 3000 50  0001 C CNN
F 3 "~" H 6300 3000 50  0001 C CNN
	1    6300 3000
	0    1    -1   0   
$EndComp
Wire Wire Line
	5800 3000 6050 3000
Wire Wire Line
	6050 3000 6050 2800
Wire Wire Line
	6050 2800 7000 2800
Connection ~ 6050 3000
Wire Wire Line
	6050 3000 6150 3000
Wire Wire Line
	5800 3100 6750 3100
Wire Wire Line
	6750 3100 6750 2900
Wire Wire Line
	6750 2900 7000 2900
Wire Wire Line
	8350 2500 7500 2500
Wire Wire Line
	8350 3100 8350 2500
Wire Wire Line
	8400 3100 8350 3100
Wire Wire Line
	7700 3000 8400 3000
Wire Wire Line
	7500 3200 8400 3200
Wire Wire Line
	7500 3300 8400 3300
$Comp
L Connector:Conn_01x04_Female 7-Segement-Anzeige
U 1 1 60BDEB10
P 8600 3100
F 0 "7-Segement-Anzeige" H 8628 3076 50  0000 L CNN
F 1 "Conn_01x04_Female" H 8628 2985 50  0000 L CNN
F 2 "" H 8600 3100 50  0001 C CNN
F 3 "~" H 8600 3100 50  0001 C CNN
	1    8600 3100
	1    0    0    -1  
$EndComp
$Comp
L Connector:Conn_01x10_Female Xbee_Right
U 1 1 60BFDC37
P 10550 3100
F 0 "Xbee_Right" H 10578 3076 50  0000 L CNN
F 1 "Conn_01x10_Female" H 10578 2985 50  0000 L CNN
F 2 "" H 10550 3100 50  0001 C CNN
F 3 "~" H 10550 3100 50  0001 C CNN
	1    10550 3100
	1    0    0    -1  
$EndComp
$Comp
L Connector:Conn_01x10_Female Xbee_Left
U 1 1 60BF92A2
P 9750 3100
F 0 "Xbee_Left" H 9778 3076 50  0000 L CNN
F 1 "Conn_01x10_Female" H 9778 2985 50  0000 L CNN
F 2 "" H 9750 3100 50  0001 C CNN
F 3 "~" H 9750 3100 50  0001 C CNN
	1    9750 3100
	1    0    0    -1  
$EndComp
Wire Wire Line
	9550 2800 7500 2800
Wire Wire Line
	7500 2900 9550 2900
Wire Wire Line
	9550 3600 9450 3600
Wire Wire Line
	9450 3600 9450 2950
Wire Wire Line
	9450 2950 7700 2950
Wire Wire Line
	7700 2950 7700 3000
Connection ~ 7700 3000
Wire Wire Line
	9550 2700 9550 2150
Wire Wire Line
	9550 2150 6700 2150
Wire Wire Line
	6700 2150 6700 2500
Connection ~ 6700 2500
Wire Wire Line
	6700 2500 7000 2500
$EndSCHEMATC
