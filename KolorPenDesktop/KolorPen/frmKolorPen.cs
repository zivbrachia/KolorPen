using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.IO;
using System.IO.Ports;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace KolorPen
{

    // to use bluetooth conneciton see the link : https://www.codeproject.com/Articles/26856/Bluetooth-Simulation-in-C-with-Serial-Ports

    public partial class frmKolorPen : Form
    {
        public enum DataMode { Text, Hex }

        private const int POWER_KNOB_MAX = 100;
        private const int POWER_KNOB_MIN = 0;
        private const int FREQUENCY_KNOB_MAX = 100;
        private const int FREQUENCY_KNOB_MIN = 0;
        private const int POWER_VOLTAGE_MAX = 5; // 5 volts
        private const int POWER_A2D_BITS_VALUE = 2048; // 11 bits
        private const int FREQUENCY_VOLTAGE_MAX = 5; // 5 volts
        private const int FREQUENCY_A2D_BITS_VALUE = 2048; // 11 bits
        private const long MAX_MESSAGE_ON_VIEW_LIST = 1000;
        private const int DEFAULT_BAUD_RATE =  9600 ;
        private long m_lNumLines = 0;
        private bool m_fStopStartLogging = false;
        private string m_sCommandbuffer = "";

        // The main control for communicating through the RS-232 port
        private SerialPort comport = new SerialPort();

        private DataMode CurrentDataMode
        {
            get
            {
                return DataMode.Text;
            }
        }

        public frmKolorPen()
        {
            InitializeComponent();
        }

        private void Form1_Load(object sender, EventArgs e)
        {
            lblFrequency.Text = knobFrequency.Value.ToString();
            lblPower.Text = knobPower.Value.ToString();
            Log(StatusTypeColor.ELogMsgType.eLOG_MSG_TYPE_ERROR, "Starting KolorPen Controller Applicaiton");
            // When data is recieved through the port, call this method
            comport.DataReceived += new SerialDataReceivedEventHandler(port_DataReceived);
        }

        private void knobFrequency_ValueChanged(object Sender)
        {
            lblFrequency.Text = knobFrequency.Value.ToString();
        }

        private void knobPower_ValueChanged(object Sender)
        {
            lblPower.Text = knobPower.Value.ToString();
        }


        /// <summary>
        /// 
        /// </summary>
        /// <param name="eLogMsgType"></param>
        /// <param name="sMsg"></param>
        /// <param name="fSaveToLog"></param>
        public void Log(StatusTypeColor.ELogMsgType eLogMsgType, string sMsg, bool fSaveToLog = false)
        {
            try
            {
                if (m_fStopStartLogging == true)
                {
                    return;
                }
                richtxtStatus.Invoke(new EventHandler(delegate
                {
                    if ((sMsg.Contains("\n") == false) && (sMsg.Contains("\r") == false))
                    {
                        sMsg += "\r\n";
                    }

                    richtxtStatus.SelectedText = string.Empty;
                    richtxtStatus.SelectionFont = new Font(richtxtStatus.SelectionFont, FontStyle.Bold);
                    richtxtStatus.SelectionColor = StatusTypeColor.GetStatusColor(eLogMsgType);
                    richtxtStatus.AppendText(sMsg);
                    richtxtStatus.ScrollToCaret();

                    //if (fSaveToLog == true)
                    //{
                    //    FileStream fileLogging = File.OpenWrite(m_sLoggingFilePathFullName);
                    //    fileLogging.Seek(0, SeekOrigin.End);
                    //    byte[] barBuffer = Encoding.ASCII.GetBytes(msg);
                    //    fileLogging.Write(barBuffer, 0, barBuffer.Length);
                    //    fileLogging.Close();
                    //}

                    m_lNumLines++;
                    if (m_lNumLines >= MAX_MESSAGE_ON_VIEW_LIST)
                    {
                        ClearStatusList();
                    }
                }));
            }
            catch (Exception)
            {
            }
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void btnClearStatus_Click(object sender, EventArgs e)
        {
            ClearStatusList();
        }

        /// <summary>
        /// 
        /// </summary>
        private void ClearStatusList()
        {
            richtxtStatus.Clear();
            m_lNumLines = 0;
        }


        private void port_DataReceived(object sender, SerialDataReceivedEventArgs e)
        {
            // If the com port has been closed, do nothing
            if (!comport.IsOpen) return;

            // This method will be called when there is data waiting in the port's buffer

            // Determain which mode (string or binary) the user is in
            if (CurrentDataMode == DataMode.Text)
            {
                // Read all the data waiting in the buffer
                string data = comport.ReadExisting();
                m_sCommandbuffer += data;
                PharseRecivedBuffer(m_sCommandbuffer);
            }
            else
            {
                // Obtain the number of bytes waiting in the port's buffer
                int bytes = comport.BytesToRead;

                // Create a byte array buffer to hold the incoming data
                byte[] buffer = new byte[bytes];

                // Read the data from the port and store it in our buffer
                comport.Read(buffer, 0, bytes);

                // Show the user the incoming data in hex format
                Log(StatusTypeColor.ELogMsgType.eLOG_MSG_TYPE_INCOMING, Utilities.ByteArrayToHexString(buffer));
            }
        }

        private void DisplayPedalStatus(bool fOnOff = false)
        {
            picbxPedalUpDown.Invoke(new EventHandler(delegate
                    {
                        picbxPedalUpDown.BackgroundImage = (fOnOff == true) ? global::KolorPen.Properties.Resources.PedalDown  : global::KolorPen.Properties.Resources.PedalUP;
                    }));
        }
        private void PharseCommand(string sMessage)
        {
            Log(StatusTypeColor.ELogMsgType.eLOG_MSG_TYPE_INCOMING, sMessage);

            if ((sMessage.Contains("p") == true) && (sMessage[0] == 'p'))
            {
                if (sMessage.Length == 4)
                {
                    if (sMessage[2] == '1') // pedal down
                    {
                        DisplayPedalStatus(true);
                    }
                    else
                    {
                        if (sMessage[2] == '0') // pedal up
                        {
                            DisplayPedalStatus(false);
                        }
                        else
                        {
                            Log(StatusTypeColor.ELogMsgType.eLOG_MSG_TYPE_ERROR, "Pedal Status is unknown : " + sMessage);
                        }
                    }
                }
            }
        }


        private void PharseRecivedBuffer(string sMessage)
        {
            if ((sMessage.Contains("\n") == true) || (sMessage.Contains("\r") == true))
            {
                bool fNewLineInLastPosiiton = sMessage[sMessage.Length - 1] == '\n';
                string[] sarSplit = sMessage.Split('\n');

                int iNumCommands = sarSplit.Length;

                if (fNewLineInLastPosiiton == false)
                {
                    iNumCommands--;
                    m_sCommandbuffer = sarSplit[sarSplit.Length - 1]; // the last entry remain as for command
                }
                else
                {
                    m_sCommandbuffer =  ""; //new buffer
                }

                for (int i = 0; i < iNumCommands; i++)
                {
                    PharseCommand(sarSplit[i] + "\n");
                }
            }
        }

        
        void OpenPort(bool bShowError)
        {
            bool error = false;
            if (comport == null)
            {
                return; //cant do anything here
            }
            // If the port is open, close it.
            if (comport.IsOpen)
            {
                try //if the usb was disconnected it could fail to close
                {
                    comport.Close();
                }
                catch
                {
                    comport = null;
                }
            }
            else
            {
                // Set the port's settings
                comport.BaudRate = DEFAULT_BAUD_RATE;
                comport.DataBits = 8;
                comport.StopBits = StopBits.One;
                comport.Parity = Parity.None;
                comport.PortName = cmbPortName.Text;
                try
                {
                    // Open the port
                    comport.Open();
                }
                catch (UnauthorizedAccessException) { error = true; }
                catch (IOException) { error = true; }
                catch (ArgumentException) { error = true; }

                if (error && bShowError)
                    MessageBox.Show(this, "Could not open the COM port.  Most likely it is already in use, has been removed, or is unavailable.", "COM Port Unavalible", MessageBoxButtons.OK, MessageBoxIcon.Stop);
            }

            // Change the state of the form's controls
            EnableControls();

            // If the port is open, send focus to the send data box
            if (comport.IsOpen)
            {
                txtSendData.Focus();
            }
        }

        private void EnableControls()
        {
            // Enable/disable controls based on whether the port is open or not
            txtSendData.Enabled = comport.IsOpen;
            btnSendFreeData.Enabled = comport.IsOpen;
            btnSendFrequency.Enabled = comport.IsOpen;
            btnPower.Enabled = comport.IsOpen;
            btnSendActivePen.Enabled = comport.IsOpen;

            btnOpenPort.Text = (comport.IsOpen) ? "Close Port" : "Open Port";
        }

        private void btnOpenPort_Click(object sender, EventArgs e)
        {
            OpenPort(true);
        }

        private void btnSendFreeData_Click(object sender, EventArgs e)
        {
            SendData();
        }

        private void SendData()
        {
            if (!comport.IsOpen)
            {
                return;
            }

            string sSend = txtSendData.Text;

            if (CurrentDataMode == DataMode.Text)
            {
                sSend += ((chkbxLineFeed.Checked == true) ? "\n" : "");
                comport.Write(sSend);
                Log(StatusTypeColor.ELogMsgType.eLOG_MSG_TYPE_OUTGOING, sSend + ((chkbxLineFeed.Checked == true) ? "" : "\n"));
            }
            else
            {
                try
                {
                    // Convert the user's string of hex digits (ex: B4 CA E2) to a byte array
                    byte[] data = Utilities.HexStringToByteArray(sSend);

                    // Send the binary data out the port
                    comport.Write(data, 0, data.Length);

                    // Show the hex digits on in the terminal window
                    Log(StatusTypeColor.ELogMsgType.eLOG_MSG_TYPE_OUTGOING, Utilities.ByteArrayToHexString(data) + "\n");
                }
                catch (FormatException)
                {
                    // Inform the user if the hex string was not properly formatted
                    Log(StatusTypeColor.ELogMsgType.eLOG_MSG_TYPE_ERROR, "Not properly formatted hex string: " + sSend + "\n");
                }
            }

            txtSendData.SelectAll();
        }

        private void btnSendFrequency_Click(object sender, EventArgs e)
        {
           // double dVoltageValue = ((double)knobFrequency.Value * (double)FREQUENCY_VOLTAGE_MAX) / (double)FREQUENCY_KNOB_MAX;
            //double dFrequencyr = (dVoltageValue * (double)FREQUENCY_A2D_BITS_VALUE) / (double)FREQUENCY_VOLTAGE_MAX;

            txtSendData.Text = "F " + ((int)knobFrequency.Value).ToString();
            SendData();
        }

        private void btnPower_Click(object sender, EventArgs e)
        {
            //double dVoltageValue = ((double)knobPower.Value * (double)POWER_VOLTAGE_MAX) / (double)POWER_KNOB_MAX;
            //double dPower = (dVoltageValue * (double)POWER_A2D_BITS_VALUE) / (double)POWER_VOLTAGE_MAX;
            double dPower = ((double)knobPower.Value * (double)1023) / (double)POWER_KNOB_MAX;

            txtSendData.Text = "O " + ((int)dPower).ToString();
            SendData();
        }

        private void RefreshComPortList()
        {
            // Determain if the list of com port names has changed since last checked
            string selected = RefreshComPortList(cmbPortName.Items.Cast<string>(), cmbPortName.SelectedItem as string, comport.IsOpen);

            // If there was an update, then update the control showing the user the list of port names
            if (!String.IsNullOrEmpty(selected))
            {
                cmbPortName.Items.Clear();
                cmbPortName.Items.AddRange(OrderedPortNames());
                cmbPortName.SelectedItem = selected;
            }
        }

        private string[] OrderedPortNames()
        {
            // Just a placeholder for a successful parsing of a string to an integer
            int num;

            // Order the serial port names in numberic order (if possible)
            return SerialPort.GetPortNames().OrderBy(a => (a.Length > 3) && int.TryParse(a.Substring(3), out num) ? num : 0).ToArray();
        }

        private string RefreshComPortList(IEnumerable<string> PreviousPortNames, string CurrentSelection, bool PortOpen)
        {
            // Create a new return report to populate
            string selected = null;

            // Retrieve the list of ports currently mounted by the operating system (sorted by name)
            string[] ports = SerialPort.GetPortNames();

            // First determain if there was a change (any additions or removals)
            bool updated = PreviousPortNames.Except(ports).Count() > 0 || ports.Except(PreviousPortNames).Count() > 0;

            // If there was a change, then select an appropriate default port
            if (updated)
            {
                // Use the correctly ordered set of port names
                ports = OrderedPortNames();

                // Find newest port if one or more were added
                string newest = SerialPort.GetPortNames().Except(PreviousPortNames).OrderBy(a => a).LastOrDefault();

                // If the port was already open... (see logic notes and reasoning in Notes.txt)
                if (PortOpen)
                {
                    if (ports.Contains(CurrentSelection)) selected = CurrentSelection;
                    else if (!String.IsNullOrEmpty(newest)) selected = newest;
                    else selected = ports.LastOrDefault();
                }
                else
                {
                    if (!String.IsNullOrEmpty(newest)) selected = newest;
                    else if (ports.Contains(CurrentSelection)) selected = CurrentSelection;
                    else selected = ports.LastOrDefault();
                }
            }

            // If there was a change to the port list, return the recommended default selection
            return selected;
        }

        private void tmrCheckComPorts_Tick(object sender, EventArgs e)
        {
            // checks to see if COM ports have been added or removed
            // since it is quite common now with USB-to-Serial adapters
            RefreshComPortList();
        }

        private void btnSendActivePen_Click(object sender, EventArgs e)
        {
            txtSendData.Text = "P " + ((radPen1.Checked == true) ? "1" : "2");
            SendData();
        }

        private void chkbxSimulationPedal_CheckedChanged(object sender, EventArgs e)
        {
            if (chkbxSimulationPedal.Checked == true)
            {
                PharseRecivedBuffer("p 1\n");
            }
            else
            {
                PharseRecivedBuffer("p 0\n");
            }
        }

        private void chkbxStopLogging_CheckedChanged(object sender, EventArgs e)
        {
            m_fStopStartLogging = chkbxStopLogging.Checked;
        }
    }


    public class StatusTypeColor
    {
        public enum EOperationStatus
        {
            eOPERATION_STATUS_NONE,
            eOPERATION_STATUS_OK,
            eOPERATION_STATUS_PASS,
            eOPERATION_STATUS_FAIL,
            eOPERATION_STATUS_RUN,
            eOPERATION_STATUS_VERIFICATION_FAILED,
            eOPERATION_STATUS_ABORT_BY_USER,
        }

        /// <summary>
        /// 
        /// </summary>
        public enum ELogMsgType
        {
            eLOG_MSG_TYPE_NONE,         // Color.Black, 
            eLOG_MSG_TYPE_INCOMING,     // Color.Blue,
            eLOG_MSG_TYPE_OUTGOING,     // Color.Brown,
            eLOG_MSG_TYPE_WARNING,      // Color.Azure,
            eLOG_MSG_TYPE_ERROR,        // Color.Red,
            eLOG_MSG_TYPE_NORMAL,       // Color.DarkGray,
            eLOG_MSG_TYPE_OK,           // Color.Green,
            eLOG_MSG_TYPE_PASS,         // Color.Green,
            eLOG_MSG_TYPE_FAIL,         // Color.Red,
            eLOG_MSG_TYPE_LOG_MESSAGE,  // Color.DarkOrange,
        }

        /// <summary>
        /// 
        /// </summary>
        public static Color[] m_clrarStatus = new Color[]
        {
            Color.Black,
            Color.Blue,
            Color.Brown,
            Color.Azure,
            Color.Red,
            Color.DarkGray,
            Color.Green,
            Color.Green,
            Color.Red,
            Color.DarkOrange,
        };

        /// <summary>
        /// 
        /// </summary>
        public static string[] m_sarOperationStatus = new string[]
        {
            "",
            "OK",
            "P",
            "F"
        };

        /// <summary>
        /// 
        /// </summary>
        public static Color[] m_clrarStatusForColor = new Color[]
        {
            Color.Black,
            Color.Black,
            Color.Black,
            Color.Black,
            Color.Yellow,
            Color.Black,
            Color.Black,
            Color.Black,
            Color.Yellow,
        };

        /// <summary>
        /// 
        /// </summary>
        public static Color[] m_clrarStatusBackColor = new Color[]
        {
            Color.White,
            Color.Blue,
            Color.Brown,
            Color.Azure,
            Color.Red,
            Color.Cyan,
            Color.Green,
            Color.Green,
            Color.Red,
        };



        /// <summary>
        /// 
        /// </summary>
        /// <param name="eOperationStatus"></param>
        /// <returns></returns>
        public static string GetOperationStatusText(EOperationStatus eOperationStatus)
        {
            return m_sarOperationStatus[(int)eOperationStatus];
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="eLogMsgType"></param>
        /// <returns></returns>
        public static Color GetStatusColor(ELogMsgType eLogMsgType)
        {
            return m_clrarStatus[(int)eLogMsgType];
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="eLogMsgType"></param>
        /// <returns></returns>
        public static Color GetStatusForColor(ELogMsgType eLogMsgType)
        {
            return m_clrarStatusForColor[(int)eLogMsgType];
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="eLogMsgType"></param>
        /// <returns></returns>
        public static Color GetStatusBackColor(ELogMsgType eLogMsgType)
        {
            return m_clrarStatusBackColor[(int)eLogMsgType];
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="eOperationStatus"></param>
        /// <returns></returns>
        public static ELogMsgType GetStatusTypeFromOperation(EOperationStatus eOperationStatus)
        {
            StatusTypeColor.ELogMsgType eLogMsgType = StatusTypeColor.ELogMsgType.eLOG_MSG_TYPE_NORMAL;

            switch (eOperationStatus)
            {
                case StatusTypeColor.EOperationStatus.eOPERATION_STATUS_OK:
                    eLogMsgType = StatusTypeColor.ELogMsgType.eLOG_MSG_TYPE_OK;
                    break;

                case StatusTypeColor.EOperationStatus.eOPERATION_STATUS_PASS:
                    eLogMsgType = StatusTypeColor.ELogMsgType.eLOG_MSG_TYPE_PASS;
                    break;

                case StatusTypeColor.EOperationStatus.eOPERATION_STATUS_FAIL:
                    eLogMsgType = StatusTypeColor.ELogMsgType.eLOG_MSG_TYPE_FAIL;
                    break;

                case StatusTypeColor.EOperationStatus.eOPERATION_STATUS_RUN:
                case StatusTypeColor.EOperationStatus.eOPERATION_STATUS_NONE:
                default:
                    eLogMsgType = StatusTypeColor.ELogMsgType.eLOG_MSG_TYPE_NORMAL;
                    break;
            }

            return eLogMsgType;
        }

    }
    /// <summary>
    /// status codes 
    /// </summary>
    public enum ESystemStatus
    {
        eSTATUS_UNKNOWN = -1,
        eSTATUS_READY = 0,
        eSTATUS_STARTED = 1,
        //...
        eSTATUS_DELIVERED = 5,
        eSTATUS_FINISH_OK = 7,
        //...
        eSTATUS_ERROR = 9
    }

    /// <summary>
    /// 
    /// </summary>
    public class StatusNames
    {
        private ESystemStatus m_eSystemStatus;
        private string m_sStatusDescripiton;

        /// <summary>
        /// 
        /// </summary>
        public ESystemStatus SystemStatus
        {
            get { return m_eSystemStatus; }
            set { m_eSystemStatus = value; }
        }

        /// <summary>
        /// 
        /// </summary>
        public string StatusDescripiton
        {
            get { return m_sStatusDescripiton; }
            set { m_sStatusDescripiton = value; }
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="eSystemStatus"></param>
        /// <param name="sStatusDescripiton"></param>
        public StatusNames(ESystemStatus eSystemStatus, string sStatusDescripiton)
        {
            SystemStatus = eSystemStatus;
            StatusDescripiton = sStatusDescripiton;
        }
    }

    public class Utilities
    {
        /************************************************************************       
                * HexStringToByteArray
                /// <summary> Convert a string of hex digits (ex: E4 CA B2) to a byte array. </summary>
                /// <param name="s"> The string containing the hex digits (with or without spaces). </param>
                /// <returns> Returns an array of bytes. </returns>
                ///     
                *
                * @return buffer of bytes     
                ************************************************************************/
        public static byte[] HexStringToByteArray(string s)
        {
            s = s.Replace(" ", "");
            if ((s.Length % 2) != 0)
            {
                s = s.Substring(1, s.Length - 1); // made it even number because each tow chars are one hex digit
            }
            byte[] buffer = new byte[s.Length / 2];
            for (int i = 0; i < s.Length; i += 2)
            {
                buffer[i / 2] = (byte)Convert.ToByte(s.Substring(i, 2), 16);
            }
            return buffer;
        }

        /************************************************************************       
        * GetDateTimeFormat -                      
        *
        * @return string - time stamp in a predefine format ""DD-MM-YYYY_hh:mm:ss"      
        ************************************************************************/
        public static string GetDateTimeFormat(DateTime datetimeValue, bool fDateOnly = false, string sSeperatorDateTime = "_", string sSeperatorTime = ":")
        {
            // Get the time stamp of general usimg format
            string sFormat = "00.##"; // format with leading zeros

            string sTimeStamp = "";

            sTimeStamp += datetimeValue.Day.ToString(sFormat) + "-" + datetimeValue.Month.ToString(sFormat) + "-" + datetimeValue.Year.ToString();
            if (fDateOnly == false)
            {
                sTimeStamp += sSeperatorDateTime; // seperates between Date & Time
                sTimeStamp += datetimeValue.Hour.ToString(sFormat) + sSeperatorTime + datetimeValue.Minute.ToString(sFormat) + sSeperatorTime + datetimeValue.Second.ToString(sFormat);
            }

            return sTimeStamp;
        }

        /************************************************************************       
        * GetDateTimeFormat -                      
        *
        * @return string - time stamp in a predefine format ""DD-MM-YYYY_hh:mm:ss"      
        ************************************************************************/
        public static string GetTimeFormat(DateTime datetimeValue, bool fUseSeconds = true, string sSeperatorTime = ":")
        {
            // Get the time stamp of general usimg format
            string sFormat = "00.##"; // format with leading zeros

            string sTimeStamp = "";

            sTimeStamp += datetimeValue.Hour.ToString(sFormat) + sSeperatorTime + datetimeValue.Minute.ToString(sFormat);

            if (fUseSeconds == true)
            {
                sTimeStamp += sSeperatorTime + datetimeValue.Second.ToString(sFormat);
            }

            return sTimeStamp;
        }

        /************************************************************************       
        * GetTimeStamp - Get a current date time                      
        *
        * @return string -       
        ************************************************************************/
        public static string GetTimeStamp()
        {
            // Get the Current time stamp of sending data to Baterry tester unit
            DateTime timeNow = DateTime.Now;

            string sTimeStamp = GetDateTimeFormat(timeNow);

            return sTimeStamp;
        }

        /************************************************************************       
        * ByteArrayToHexString
        /// <summary> Converts an array of bytes into a formatted string of hex digits (ex: E4 CA B2)</summary>
        /// <param name="data"> The array of bytes to be translated into a string of hex digits. </param>
        /// <returns> Returns a well formatted string of hex digits with spacing. </returns>  
        *
        * @return string     
        ************************************************************************/
        public static string ByteArrayToHexString(byte[] data, int iLen = 0)
        {
            int iMaxLen = (iLen == 0) ? data.Length : iLen;
            StringBuilder sb = new StringBuilder(iMaxLen * 3);
            for (int i = 0; i < iMaxLen; i++)
            //foreach (byte b in data)
            {
                sb.Append(Convert.ToString(data[i], 16).PadLeft(2, '0').PadRight(3, ' '));
            }
            return sb.ToString().ToUpper();
        }
        /************************************************************************       
        * ByteArrayToHexString
        /// <summary> Converts an array of bytes into a formatted string of hex digits (ex: E4 CA B2)</summary>
        /// <param name="data"> The array of bytes to be translated into a string of hex digits. </param>
        /// <returns> Returns a well formatted string of hex digits with spacing. </returns>  
        *
        * @return string     
        ************************************************************************/
        public static string ByteArrayToHexString(byte[] data, int iOffset, int iLen = 0)
        {
            int iMaxLen = (iLen == 0) ? data.Length - iOffset : iLen;
            StringBuilder sb = new StringBuilder(iMaxLen * 3);
            for (int i = 0; i < iMaxLen; i++)
            //foreach (byte b in data)
            {
                sb.Append(Convert.ToString(data[i + iOffset], 16).PadLeft(2, '0').PadRight(3, ' '));
            }
            return sb.ToString().ToUpper();
        }


        /************************************************************************       
        * ByteArrayToHexString
        * @Param string sLoggingFilePathFullName
        * @Param string sMsg
        *
        * @return string     
        ************************************************************************/
        public static void SaveToLogFile(string sLoggingFilePathFullName, string sMsg)
        {
            // check if the Msg has a new line as the first char remove it because we wand the Time stamp to be in the same lin
            if (sMsg[0] == '\n')
            {
                sMsg = sMsg.Substring(1, sMsg.Length - 1);
            }

            string sLogMessage = "[ " + GetTimeStamp() + " ] : \t" + sMsg;

            try
            {
                FileStream fileLogging = File.OpenWrite(sLoggingFilePathFullName);
                fileLogging.Seek(0, SeekOrigin.End);
                byte[] barBuffer = Encoding.ASCII.GetBytes(sLogMessage);
                fileLogging.Write(barBuffer, 0, barBuffer.Length);
                fileLogging.Close();
            }
            catch (Exception ex)
            {
                //
            }
        }
    }
}
