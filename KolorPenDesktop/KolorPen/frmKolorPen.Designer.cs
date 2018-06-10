namespace KolorPen
{
    partial class frmKolorPen
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.components = new System.ComponentModel.Container();
            PictureBoxEx.ObjectProperty objectProperty1 = new PictureBoxEx.ObjectProperty();
            PictureBoxEx.ObjectProperty objectProperty2 = new PictureBoxEx.ObjectProperty();
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(frmKolorPen));
            this.grpbxexSelectActivePen = new GroupBoxEx.GroupBoxEx();
            this.picbxPen = new PictureBoxEx.PictureBoxEx();
            this.btnSendActivePen = new System.Windows.Forms.Button();
            this.radPen2 = new System.Windows.Forms.RadioButton();
            this.radPen1 = new System.Windows.Forms.RadioButton();
            this.lblSelectPen = new System.Windows.Forms.Label();
            this.knobFrequency = new KnobControl.KnobControl();
            this.grpbxexFrequency = new GroupBoxEx.GroupBoxEx();
            this.lblFrequencyValue = new System.Windows.Forms.Label();
            this.lblFrequency = new System.Windows.Forms.Label();
            this.btnSendFrequency = new System.Windows.Forms.Button();
            this.grpbxexPower = new GroupBoxEx.GroupBoxEx();
            this.lblPowerValue = new System.Windows.Forms.Label();
            this.lblPower = new System.Windows.Forms.Label();
            this.knobPower = new KnobControl.KnobControl();
            this.btnPower = new System.Windows.Forms.Button();
            this.btnSendFreeData = new System.Windows.Forms.Button();
            this.grpbxexPedalStatus = new GroupBoxEx.GroupBoxEx();
            this.chkbxSimulationPedal = new System.Windows.Forms.CheckBox();
            this.picbxPedalUpDown = new PictureBoxEx.PictureBoxEx();
            this.picbxLogo = new System.Windows.Forms.PictureBox();
            this.grpbxexCurrentCommand = new GroupBoxEx.GroupBoxEx();
            this.richtxtStatus = new System.Windows.Forms.RichTextBox();
            this.btnClearStatus = new System.Windows.Forms.Button();
            this.lblComPort = new System.Windows.Forms.Label();
            this.cmbPortName = new System.Windows.Forms.ComboBox();
            this.btnOpenPort = new System.Windows.Forms.Button();
            this.chkbxLineFeed = new System.Windows.Forms.CheckBox();
            this.grpbxCommunicationSettings = new GroupBoxEx.GroupBoxEx();
            this.txtSendData = new System.Windows.Forms.TextBox();
            this.label1 = new System.Windows.Forms.Label();
            this.tmrCheckComPorts = new System.Windows.Forms.Timer(this.components);
            this.chkbxStopLogging = new System.Windows.Forms.CheckBox();
            this.grpbxexSelectActivePen.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.picbxPen)).BeginInit();
            this.grpbxexFrequency.SuspendLayout();
            this.grpbxexPower.SuspendLayout();
            this.grpbxexPedalStatus.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.picbxPedalUpDown)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.picbxLogo)).BeginInit();
            this.grpbxexCurrentCommand.SuspendLayout();
            this.grpbxCommunicationSettings.SuspendLayout();
            this.SuspendLayout();
            // 
            // grpbxexSelectActivePen
            // 
            this.grpbxexSelectActivePen.BackgroundPanelImage = null;
            this.grpbxexSelectActivePen.Controls.Add(this.picbxPen);
            this.grpbxexSelectActivePen.Controls.Add(this.btnSendActivePen);
            this.grpbxexSelectActivePen.Controls.Add(this.radPen2);
            this.grpbxexSelectActivePen.Controls.Add(this.radPen1);
            this.grpbxexSelectActivePen.Controls.Add(this.lblSelectPen);
            this.grpbxexSelectActivePen.DrawGroupBorder = true;
            this.grpbxexSelectActivePen.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(177)));
            this.grpbxexSelectActivePen.ForeColor = System.Drawing.Color.Snow;
            this.grpbxexSelectActivePen.GroupBorderColor = System.Drawing.Color.Black;
            this.grpbxexSelectActivePen.GroupPanelColor = System.Drawing.SystemColors.GradientActiveCaption;
            this.grpbxexSelectActivePen.GroupPanelShape = GroupBoxEx.GroupBoxEx.PanelType.Rounded;
            this.grpbxexSelectActivePen.Location = new System.Drawing.Point(12, 401);
            this.grpbxexSelectActivePen.Name = "grpbxexSelectActivePen";
            this.grpbxexSelectActivePen.Size = new System.Drawing.Size(247, 80);
            this.grpbxexSelectActivePen.TabIndex = 17;
            this.grpbxexSelectActivePen.TabStop = false;
            this.grpbxexSelectActivePen.Text = "Select Active Pen";
            this.grpbxexSelectActivePen.TextBackColor = System.Drawing.Color.LightSeaGreen;
            this.grpbxexSelectActivePen.TextBorderColor = System.Drawing.Color.Maroon;
            // 
            // picbxPen
            // 
            this.picbxPen.BackColor = System.Drawing.Color.Transparent;
            this.picbxPen.BackgroundImage = global::KolorPen.Properties.Resources.Pen;
            this.picbxPen.BackgroundImageLayout = System.Windows.Forms.ImageLayout.Stretch;
            this.picbxPen.BorderColor = System.Drawing.Color.Red;
            this.picbxPen.BorderMoveSize = 3;
            this.picbxPen.BorderWidth = 1;
            this.picbxPen.Color1Transparent = 20;
            this.picbxPen.Color2Transparent = 20;
            this.picbxPen.Cursor = System.Windows.Forms.Cursors.Hand;
            this.picbxPen.GradientColor1 = System.Drawing.Color.LightGreen;
            this.picbxPen.GradientColor2 = System.Drawing.Color.DarkBlue;
            this.picbxPen.Location = new System.Drawing.Point(5, 26);
            this.picbxPen.Margin = new System.Windows.Forms.Padding(0);
            this.picbxPen.Name = "picbxPen";
            this.picbxPen.NewLocation = new System.Drawing.Point(5, 26);
            objectProperty1.ClickEventEnbale = false;
            objectProperty1.ClickImageFilename = "";
            objectProperty1.DoubleClickEventEnbale = false;
            objectProperty1.DoubleClickImageFilename = "";
            objectProperty1.Name = "";
            this.picbxPen.ObjectProperties = objectProperty1;
            this.picbxPen.Size = new System.Drawing.Size(27, 44);
            this.picbxPen.TabIndex = 14;
            this.picbxPen.TabStop = false;
            // 
            // btnSendActivePen
            // 
            this.btnSendActivePen.Enabled = false;
            this.btnSendActivePen.ForeColor = System.Drawing.Color.DarkRed;
            this.btnSendActivePen.Location = new System.Drawing.Point(169, 50);
            this.btnSendActivePen.Name = "btnSendActivePen";
            this.btnSendActivePen.Size = new System.Drawing.Size(62, 27);
            this.btnSendActivePen.TabIndex = 13;
            this.btnSendActivePen.Text = "Send >>";
            this.btnSendActivePen.UseVisualStyleBackColor = true;
            this.btnSendActivePen.Click += new System.EventHandler(this.btnSendActivePen_Click);
            // 
            // radPen2
            // 
            this.radPen2.AutoSize = true;
            this.radPen2.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F);
            this.radPen2.ForeColor = System.Drawing.Color.Maroon;
            this.radPen2.Location = new System.Drawing.Point(169, 30);
            this.radPen2.Name = "radPen2";
            this.radPen2.Size = new System.Drawing.Size(53, 17);
            this.radPen2.TabIndex = 12;
            this.radPen2.Text = "Pen 2";
            this.radPen2.UseVisualStyleBackColor = true;
            // 
            // radPen1
            // 
            this.radPen1.AutoSize = true;
            this.radPen1.BackColor = System.Drawing.Color.Transparent;
            this.radPen1.Checked = true;
            this.radPen1.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F);
            this.radPen1.ForeColor = System.Drawing.Color.Maroon;
            this.radPen1.Location = new System.Drawing.Point(106, 30);
            this.radPen1.Name = "radPen1";
            this.radPen1.Size = new System.Drawing.Size(53, 17);
            this.radPen1.TabIndex = 12;
            this.radPen1.TabStop = true;
            this.radPen1.Text = "Pen 1";
            this.radPen1.UseVisualStyleBackColor = false;
            // 
            // lblSelectPen
            // 
            this.lblSelectPen.AutoSize = true;
            this.lblSelectPen.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(177)));
            this.lblSelectPen.ForeColor = System.Drawing.Color.Maroon;
            this.lblSelectPen.Location = new System.Drawing.Point(33, 31);
            this.lblSelectPen.Name = "lblSelectPen";
            this.lblSelectPen.Size = new System.Drawing.Size(59, 13);
            this.lblSelectPen.TabIndex = 11;
            this.lblSelectPen.Text = "Select Pen";
            // 
            // knobFrequency
            // 
            this.knobFrequency.EndAngle = 405F;
            this.knobFrequency.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(177)));
            this.knobFrequency.ForeColor = System.Drawing.Color.Transparent;
            this.knobFrequency.ImeMode = System.Windows.Forms.ImeMode.On;
            this.knobFrequency.knobBackColor = System.Drawing.Color.PeachPuff;
            this.knobFrequency.KnobPointerStyle = KnobControl.KnobControl.knobPointerStyle.circle;
            this.knobFrequency.LargeChange = 5;
            this.knobFrequency.Location = new System.Drawing.Point(13, 30);
            this.knobFrequency.Maximum = 100;
            this.knobFrequency.Minimum = 0;
            this.knobFrequency.Name = "knobFrequency";
            this.knobFrequency.PointerColor = System.Drawing.Color.SlateBlue;
            this.knobFrequency.ScaleColor = System.Drawing.Color.Black;
            this.knobFrequency.ScaleDivisions = 11;
            this.knobFrequency.ScaleSubDivisions = 4;
            this.knobFrequency.ShowLargeScale = true;
            this.knobFrequency.ShowSmallScale = false;
            this.knobFrequency.Size = new System.Drawing.Size(150, 150);
            this.knobFrequency.SmallChange = 1;
            this.knobFrequency.StartAngle = 135F;
            this.knobFrequency.TabIndex = 18;
            this.knobFrequency.Value = 0;
            this.knobFrequency.ValueChanged += new KnobControl.ValueChangedEventHandler(this.knobFrequency_ValueChanged);
            // 
            // grpbxexFrequency
            // 
            this.grpbxexFrequency.BackgroundPanelImage = null;
            this.grpbxexFrequency.Controls.Add(this.lblFrequencyValue);
            this.grpbxexFrequency.Controls.Add(this.lblFrequency);
            this.grpbxexFrequency.Controls.Add(this.knobFrequency);
            this.grpbxexFrequency.Controls.Add(this.btnSendFrequency);
            this.grpbxexFrequency.DrawGroupBorder = true;
            this.grpbxexFrequency.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(177)));
            this.grpbxexFrequency.ForeColor = System.Drawing.Color.Snow;
            this.grpbxexFrequency.GroupBorderColor = System.Drawing.Color.Black;
            this.grpbxexFrequency.GroupPanelColor = System.Drawing.SystemColors.GradientActiveCaption;
            this.grpbxexFrequency.GroupPanelShape = GroupBoxEx.GroupBoxEx.PanelType.Rounded;
            this.grpbxexFrequency.Location = new System.Drawing.Point(12, 195);
            this.grpbxexFrequency.Name = "grpbxexFrequency";
            this.grpbxexFrequency.Size = new System.Drawing.Size(247, 192);
            this.grpbxexFrequency.TabIndex = 17;
            this.grpbxexFrequency.TabStop = false;
            this.grpbxexFrequency.Text = "Frequency";
            this.grpbxexFrequency.TextBackColor = System.Drawing.Color.LightSeaGreen;
            this.grpbxexFrequency.TextBorderColor = System.Drawing.Color.Maroon;
            // 
            // lblFrequencyValue
            // 
            this.lblFrequencyValue.AutoSize = true;
            this.lblFrequencyValue.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(177)));
            this.lblFrequencyValue.ForeColor = System.Drawing.Color.Brown;
            this.lblFrequencyValue.Location = new System.Drawing.Point(176, 62);
            this.lblFrequencyValue.Name = "lblFrequencyValue";
            this.lblFrequencyValue.Size = new System.Drawing.Size(57, 13);
            this.lblFrequencyValue.TabIndex = 18;
            this.lblFrequencyValue.Text = "Frequency";
            // 
            // lblFrequency
            // 
            this.lblFrequency.Font = new System.Drawing.Font("Microsoft Sans Serif", 14.25F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(177)));
            this.lblFrequency.ForeColor = System.Drawing.Color.DarkOliveGreen;
            this.lblFrequency.Location = new System.Drawing.Point(176, 82);
            this.lblFrequency.Name = "lblFrequency";
            this.lblFrequency.Size = new System.Drawing.Size(59, 27);
            this.lblFrequency.TabIndex = 18;
            this.lblFrequency.Text = "0";
            this.lblFrequency.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
            // 
            // btnSendFrequency
            // 
            this.btnSendFrequency.Enabled = false;
            this.btnSendFrequency.ForeColor = System.Drawing.Color.DarkRed;
            this.btnSendFrequency.Location = new System.Drawing.Point(173, 128);
            this.btnSendFrequency.Name = "btnSendFrequency";
            this.btnSendFrequency.Size = new System.Drawing.Size(62, 27);
            this.btnSendFrequency.TabIndex = 13;
            this.btnSendFrequency.Text = "Send >>";
            this.btnSendFrequency.UseVisualStyleBackColor = true;
            this.btnSendFrequency.Click += new System.EventHandler(this.btnSendFrequency_Click);
            // 
            // grpbxexPower
            // 
            this.grpbxexPower.BackgroundPanelImage = null;
            this.grpbxexPower.Controls.Add(this.lblPowerValue);
            this.grpbxexPower.Controls.Add(this.lblPower);
            this.grpbxexPower.Controls.Add(this.knobPower);
            this.grpbxexPower.Controls.Add(this.btnPower);
            this.grpbxexPower.DrawGroupBorder = true;
            this.grpbxexPower.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(177)));
            this.grpbxexPower.ForeColor = System.Drawing.Color.Snow;
            this.grpbxexPower.GroupBorderColor = System.Drawing.Color.Black;
            this.grpbxexPower.GroupPanelColor = System.Drawing.SystemColors.GradientActiveCaption;
            this.grpbxexPower.GroupPanelShape = GroupBoxEx.GroupBoxEx.PanelType.Rounded;
            this.grpbxexPower.Location = new System.Drawing.Point(283, 195);
            this.grpbxexPower.Name = "grpbxexPower";
            this.grpbxexPower.Size = new System.Drawing.Size(247, 192);
            this.grpbxexPower.TabIndex = 17;
            this.grpbxexPower.TabStop = false;
            this.grpbxexPower.Text = "Power";
            this.grpbxexPower.TextBackColor = System.Drawing.Color.LightSeaGreen;
            this.grpbxexPower.TextBorderColor = System.Drawing.Color.Maroon;
            // 
            // lblPowerValue
            // 
            this.lblPowerValue.AutoSize = true;
            this.lblPowerValue.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(177)));
            this.lblPowerValue.ForeColor = System.Drawing.Color.Brown;
            this.lblPowerValue.Location = new System.Drawing.Point(188, 62);
            this.lblPowerValue.Name = "lblPowerValue";
            this.lblPowerValue.Size = new System.Drawing.Size(37, 13);
            this.lblPowerValue.TabIndex = 18;
            this.lblPowerValue.Text = "Power";
            // 
            // lblPower
            // 
            this.lblPower.Font = new System.Drawing.Font("Microsoft Sans Serif", 14.25F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(177)));
            this.lblPower.ForeColor = System.Drawing.Color.DarkOliveGreen;
            this.lblPower.Location = new System.Drawing.Point(176, 82);
            this.lblPower.Name = "lblPower";
            this.lblPower.Size = new System.Drawing.Size(59, 27);
            this.lblPower.TabIndex = 18;
            this.lblPower.Text = "0";
            this.lblPower.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
            // 
            // knobPower
            // 
            this.knobPower.EndAngle = 405F;
            this.knobPower.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(177)));
            this.knobPower.ForeColor = System.Drawing.Color.Transparent;
            this.knobPower.ImeMode = System.Windows.Forms.ImeMode.On;
            this.knobPower.knobBackColor = System.Drawing.Color.OrangeRed;
            this.knobPower.KnobPointerStyle = KnobControl.KnobControl.knobPointerStyle.circle;
            this.knobPower.LargeChange = 5;
            this.knobPower.Location = new System.Drawing.Point(13, 30);
            this.knobPower.Maximum = 100;
            this.knobPower.Minimum = 0;
            this.knobPower.Name = "knobPower";
            this.knobPower.PointerColor = System.Drawing.Color.Yellow;
            this.knobPower.ScaleColor = System.Drawing.Color.Black;
            this.knobPower.ScaleDivisions = 11;
            this.knobPower.ScaleSubDivisions = 4;
            this.knobPower.ShowLargeScale = true;
            this.knobPower.ShowSmallScale = false;
            this.knobPower.Size = new System.Drawing.Size(150, 150);
            this.knobPower.SmallChange = 1;
            this.knobPower.StartAngle = 135F;
            this.knobPower.TabIndex = 18;
            this.knobPower.Value = 0;
            this.knobPower.ValueChanged += new KnobControl.ValueChangedEventHandler(this.knobPower_ValueChanged);
            // 
            // btnPower
            // 
            this.btnPower.Enabled = false;
            this.btnPower.ForeColor = System.Drawing.Color.DarkRed;
            this.btnPower.Location = new System.Drawing.Point(173, 128);
            this.btnPower.Name = "btnPower";
            this.btnPower.Size = new System.Drawing.Size(62, 27);
            this.btnPower.TabIndex = 13;
            this.btnPower.Text = "Send >>";
            this.btnPower.UseVisualStyleBackColor = true;
            this.btnPower.Click += new System.EventHandler(this.btnPower_Click);
            // 
            // btnSendFreeData
            // 
            this.btnSendFreeData.Enabled = false;
            this.btnSendFreeData.ForeColor = System.Drawing.Color.DarkRed;
            this.btnSendFreeData.Location = new System.Drawing.Point(429, 67);
            this.btnSendFreeData.Name = "btnSendFreeData";
            this.btnSendFreeData.Size = new System.Drawing.Size(62, 27);
            this.btnSendFreeData.TabIndex = 13;
            this.btnSendFreeData.Text = "Send >>";
            this.btnSendFreeData.UseVisualStyleBackColor = true;
            this.btnSendFreeData.Click += new System.EventHandler(this.btnSendFreeData_Click);
            // 
            // grpbxexPedalStatus
            // 
            this.grpbxexPedalStatus.BackgroundPanelImage = null;
            this.grpbxexPedalStatus.Controls.Add(this.chkbxSimulationPedal);
            this.grpbxexPedalStatus.Controls.Add(this.picbxPedalUpDown);
            this.grpbxexPedalStatus.DrawGroupBorder = true;
            this.grpbxexPedalStatus.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(177)));
            this.grpbxexPedalStatus.ForeColor = System.Drawing.Color.Snow;
            this.grpbxexPedalStatus.GroupBorderColor = System.Drawing.Color.Black;
            this.grpbxexPedalStatus.GroupPanelColor = System.Drawing.SystemColors.GradientActiveCaption;
            this.grpbxexPedalStatus.GroupPanelShape = GroupBoxEx.GroupBoxEx.PanelType.Rounded;
            this.grpbxexPedalStatus.Location = new System.Drawing.Point(346, 401);
            this.grpbxexPedalStatus.Name = "grpbxexPedalStatus";
            this.grpbxexPedalStatus.Size = new System.Drawing.Size(111, 80);
            this.grpbxexPedalStatus.TabIndex = 17;
            this.grpbxexPedalStatus.TabStop = false;
            this.grpbxexPedalStatus.Text = "Pedal Status";
            this.grpbxexPedalStatus.TextBackColor = System.Drawing.Color.LightSeaGreen;
            this.grpbxexPedalStatus.TextBorderColor = System.Drawing.Color.Maroon;
            // 
            // chkbxSimulationPedal
            // 
            this.chkbxSimulationPedal.AutoSize = true;
            this.chkbxSimulationPedal.Location = new System.Drawing.Point(88, 42);
            this.chkbxSimulationPedal.Name = "chkbxSimulationPedal";
            this.chkbxSimulationPedal.Size = new System.Drawing.Size(15, 14);
            this.chkbxSimulationPedal.TabIndex = 15;
            this.chkbxSimulationPedal.UseVisualStyleBackColor = true;
            this.chkbxSimulationPedal.CheckedChanged += new System.EventHandler(this.chkbxSimulationPedal_CheckedChanged);
            // 
            // picbxPedalUpDown
            // 
            this.picbxPedalUpDown.BackColor = System.Drawing.Color.Transparent;
            this.picbxPedalUpDown.BackgroundImage = global::KolorPen.Properties.Resources.PedalUP;
            this.picbxPedalUpDown.BackgroundImageLayout = System.Windows.Forms.ImageLayout.Stretch;
            this.picbxPedalUpDown.BorderColor = System.Drawing.Color.Red;
            this.picbxPedalUpDown.BorderMoveSize = 3;
            this.picbxPedalUpDown.BorderWidth = 1;
            this.picbxPedalUpDown.Color1Transparent = 20;
            this.picbxPedalUpDown.Color2Transparent = 20;
            this.picbxPedalUpDown.Cursor = System.Windows.Forms.Cursors.Hand;
            this.picbxPedalUpDown.GradientColor1 = System.Drawing.Color.LightGreen;
            this.picbxPedalUpDown.GradientColor2 = System.Drawing.Color.DarkBlue;
            this.picbxPedalUpDown.Location = new System.Drawing.Point(27, 23);
            this.picbxPedalUpDown.Margin = new System.Windows.Forms.Padding(0);
            this.picbxPedalUpDown.Name = "picbxPedalUpDown";
            this.picbxPedalUpDown.NewLocation = new System.Drawing.Point(27, 23);
            objectProperty2.ClickEventEnbale = false;
            objectProperty2.ClickImageFilename = "";
            objectProperty2.DoubleClickEventEnbale = false;
            objectProperty2.DoubleClickImageFilename = "";
            objectProperty2.Name = "";
            this.picbxPedalUpDown.ObjectProperties = objectProperty2;
            this.picbxPedalUpDown.Size = new System.Drawing.Size(56, 51);
            this.picbxPedalUpDown.TabIndex = 14;
            this.picbxPedalUpDown.TabStop = false;
            // 
            // picbxLogo
            // 
            this.picbxLogo.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.picbxLogo.BackgroundImage = global::KolorPen.Properties.Resources.KolorPenLogo;
            this.picbxLogo.BackgroundImageLayout = System.Windows.Forms.ImageLayout.Stretch;
            this.picbxLogo.Location = new System.Drawing.Point(0, 1);
            this.picbxLogo.Name = "picbxLogo";
            this.picbxLogo.Size = new System.Drawing.Size(543, 188);
            this.picbxLogo.TabIndex = 0;
            this.picbxLogo.TabStop = false;
            // 
            // grpbxexCurrentCommand
            // 
            this.grpbxexCurrentCommand.BackgroundPanelImage = null;
            this.grpbxexCurrentCommand.Controls.Add(this.richtxtStatus);
            this.grpbxexCurrentCommand.Controls.Add(this.chkbxStopLogging);
            this.grpbxexCurrentCommand.Controls.Add(this.btnClearStatus);
            this.grpbxexCurrentCommand.DrawGroupBorder = true;
            this.grpbxexCurrentCommand.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(177)));
            this.grpbxexCurrentCommand.ForeColor = System.Drawing.Color.Maroon;
            this.grpbxexCurrentCommand.GroupBorderColor = System.Drawing.Color.Black;
            this.grpbxexCurrentCommand.GroupPanelColor = System.Drawing.SystemColors.GradientActiveCaption;
            this.grpbxexCurrentCommand.GroupPanelShape = GroupBoxEx.GroupBoxEx.PanelType.Rounded;
            this.grpbxexCurrentCommand.Location = new System.Drawing.Point(12, 602);
            this.grpbxexCurrentCommand.Margin = new System.Windows.Forms.Padding(4);
            this.grpbxexCurrentCommand.Name = "grpbxexCurrentCommand";
            this.grpbxexCurrentCommand.Padding = new System.Windows.Forms.Padding(4);
            this.grpbxexCurrentCommand.Size = new System.Drawing.Size(518, 201);
            this.grpbxexCurrentCommand.TabIndex = 51;
            this.grpbxexCurrentCommand.TabStop = false;
            this.grpbxexCurrentCommand.Text = "Status Current Commands";
            this.grpbxexCurrentCommand.TextBackColor = System.Drawing.Color.Gold;
            this.grpbxexCurrentCommand.TextBorderColor = System.Drawing.Color.Maroon;
            // 
            // richtxtStatus
            // 
            this.richtxtStatus.Location = new System.Drawing.Point(5, 22);
            this.richtxtStatus.Margin = new System.Windows.Forms.Padding(3, 2, 3, 2);
            this.richtxtStatus.Name = "richtxtStatus";
            this.richtxtStatus.Size = new System.Drawing.Size(471, 169);
            this.richtxtStatus.TabIndex = 21;
            this.richtxtStatus.Text = "";
            // 
            // btnClearStatus
            // 
            this.btnClearStatus.Image = ((System.Drawing.Image)(resources.GetObject("btnClearStatus.Image")));
            this.btnClearStatus.Location = new System.Drawing.Point(478, 156);
            this.btnClearStatus.Margin = new System.Windows.Forms.Padding(4);
            this.btnClearStatus.Name = "btnClearStatus";
            this.btnClearStatus.Size = new System.Drawing.Size(32, 32);
            this.btnClearStatus.TabIndex = 20;
            this.btnClearStatus.UseVisualStyleBackColor = true;
            this.btnClearStatus.Click += new System.EventHandler(this.btnClearStatus_Click);
            // 
            // lblComPort
            // 
            this.lblComPort.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Right)));
            this.lblComPort.AutoSize = true;
            this.lblComPort.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.lblComPort.ForeColor = System.Drawing.Color.DarkRed;
            this.lblComPort.Location = new System.Drawing.Point(18, 34);
            this.lblComPort.Name = "lblComPort";
            this.lblComPort.Size = new System.Drawing.Size(108, 13);
            this.lblComPort.TabIndex = 2;
            this.lblComPort.Text = "Selectg COM Port";
            // 
            // cmbPortName
            // 
            this.cmbPortName.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Right)));
            this.cmbPortName.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.cmbPortName.FormattingEnabled = true;
            this.cmbPortName.Items.AddRange(new object[] {
            "COM1",
            "COM2",
            "COM3",
            "COM4",
            "COM5",
            "COM6"});
            this.cmbPortName.Location = new System.Drawing.Point(131, 31);
            this.cmbPortName.Name = "cmbPortName";
            this.cmbPortName.Size = new System.Drawing.Size(116, 21);
            this.cmbPortName.TabIndex = 3;
            // 
            // btnOpenPort
            // 
            this.btnOpenPort.ForeColor = System.Drawing.Color.DarkRed;
            this.btnOpenPort.Location = new System.Drawing.Point(356, 27);
            this.btnOpenPort.Name = "btnOpenPort";
            this.btnOpenPort.Size = new System.Drawing.Size(115, 27);
            this.btnOpenPort.TabIndex = 13;
            this.btnOpenPort.Text = "Open Port";
            this.btnOpenPort.UseVisualStyleBackColor = true;
            this.btnOpenPort.Click += new System.EventHandler(this.btnOpenPort_Click);
            // 
            // chkbxLineFeed
            // 
            this.chkbxLineFeed.AutoSize = true;
            this.chkbxLineFeed.Checked = true;
            this.chkbxLineFeed.CheckState = System.Windows.Forms.CheckState.Checked;
            this.chkbxLineFeed.ForeColor = System.Drawing.Color.DarkRed;
            this.chkbxLineFeed.Location = new System.Drawing.Point(266, 33);
            this.chkbxLineFeed.Name = "chkbxLineFeed";
            this.chkbxLineFeed.Size = new System.Drawing.Size(40, 17);
            this.chkbxLineFeed.TabIndex = 4;
            this.chkbxLineFeed.Text = "LF";
            this.chkbxLineFeed.UseVisualStyleBackColor = true;
            // 
            // grpbxCommunicationSettings
            // 
            this.grpbxCommunicationSettings.BackgroundPanelImage = null;
            this.grpbxCommunicationSettings.Controls.Add(this.txtSendData);
            this.grpbxCommunicationSettings.Controls.Add(this.chkbxLineFeed);
            this.grpbxCommunicationSettings.Controls.Add(this.btnOpenPort);
            this.grpbxCommunicationSettings.Controls.Add(this.btnSendFreeData);
            this.grpbxCommunicationSettings.Controls.Add(this.cmbPortName);
            this.grpbxCommunicationSettings.Controls.Add(this.label1);
            this.grpbxCommunicationSettings.Controls.Add(this.lblComPort);
            this.grpbxCommunicationSettings.DrawGroupBorder = true;
            this.grpbxCommunicationSettings.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(177)));
            this.grpbxCommunicationSettings.ForeColor = System.Drawing.Color.Snow;
            this.grpbxCommunicationSettings.GroupBorderColor = System.Drawing.Color.Black;
            this.grpbxCommunicationSettings.GroupPanelColor = System.Drawing.SystemColors.GradientActiveCaption;
            this.grpbxCommunicationSettings.GroupPanelShape = GroupBoxEx.GroupBoxEx.PanelType.Rounded;
            this.grpbxCommunicationSettings.Location = new System.Drawing.Point(12, 495);
            this.grpbxCommunicationSettings.Name = "grpbxCommunicationSettings";
            this.grpbxCommunicationSettings.Size = new System.Drawing.Size(518, 100);
            this.grpbxCommunicationSettings.TabIndex = 17;
            this.grpbxCommunicationSettings.TabStop = false;
            this.grpbxCommunicationSettings.Text = "Communication settings";
            this.grpbxCommunicationSettings.TextBackColor = System.Drawing.Color.Olive;
            this.grpbxCommunicationSettings.TextBorderColor = System.Drawing.Color.Maroon;
            // 
            // txtSendData
            // 
            this.txtSendData.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Right)));
            this.txtSendData.Location = new System.Drawing.Point(131, 71);
            this.txtSendData.Name = "txtSendData";
            this.txtSendData.Size = new System.Drawing.Size(286, 20);
            this.txtSendData.TabIndex = 14;
            // 
            // label1
            // 
            this.label1.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Right)));
            this.label1.AutoSize = true;
            this.label1.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label1.ForeColor = System.Drawing.Color.DarkRed;
            this.label1.Location = new System.Drawing.Point(18, 74);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(96, 13);
            this.label1.TabIndex = 2;
            this.label1.Text = "Free Send Data";
            // 
            // tmrCheckComPorts
            // 
            this.tmrCheckComPorts.Enabled = true;
            this.tmrCheckComPorts.Interval = 500;
            this.tmrCheckComPorts.Tick += new System.EventHandler(this.tmrCheckComPorts_Tick);
            // 
            // chkbxStopLogging
            // 
            this.chkbxStopLogging.AutoSize = true;
            this.chkbxStopLogging.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(177)));
            this.chkbxStopLogging.ForeColor = System.Drawing.Color.DarkRed;
            this.chkbxStopLogging.Location = new System.Drawing.Point(477, 62);
            this.chkbxStopLogging.Name = "chkbxStopLogging";
            this.chkbxStopLogging.Size = new System.Drawing.Size(44, 17);
            this.chkbxStopLogging.TabIndex = 4;
            this.chkbxStopLogging.Text = "Log";
            this.chkbxStopLogging.UseVisualStyleBackColor = true;
            this.chkbxStopLogging.CheckedChanged += new System.EventHandler(this.chkbxStopLogging_CheckedChanged);
            // 
            // frmKolorPen
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(543, 816);
            this.Controls.Add(this.grpbxexCurrentCommand);
            this.Controls.Add(this.grpbxexPower);
            this.Controls.Add(this.grpbxexFrequency);
            this.Controls.Add(this.grpbxCommunicationSettings);
            this.Controls.Add(this.grpbxexPedalStatus);
            this.Controls.Add(this.grpbxexSelectActivePen);
            this.Controls.Add(this.picbxLogo);
            this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
            this.Name = "frmKolorPen";
            this.Text = "Kolor Pen Controller";
            this.Load += new System.EventHandler(this.Form1_Load);
            this.grpbxexSelectActivePen.ResumeLayout(false);
            this.grpbxexSelectActivePen.PerformLayout();
            ((System.ComponentModel.ISupportInitialize)(this.picbxPen)).EndInit();
            this.grpbxexFrequency.ResumeLayout(false);
            this.grpbxexFrequency.PerformLayout();
            this.grpbxexPower.ResumeLayout(false);
            this.grpbxexPower.PerformLayout();
            this.grpbxexPedalStatus.ResumeLayout(false);
            this.grpbxexPedalStatus.PerformLayout();
            ((System.ComponentModel.ISupportInitialize)(this.picbxPedalUpDown)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.picbxLogo)).EndInit();
            this.grpbxexCurrentCommand.ResumeLayout(false);
            this.grpbxexCurrentCommand.PerformLayout();
            this.grpbxCommunicationSettings.ResumeLayout(false);
            this.grpbxCommunicationSettings.PerformLayout();
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.PictureBox picbxLogo;
        private GroupBoxEx.GroupBoxEx grpbxexSelectActivePen;
        private PictureBoxEx.PictureBoxEx picbxPen;
        private System.Windows.Forms.Button btnSendActivePen;
        private System.Windows.Forms.RadioButton radPen2;
        private System.Windows.Forms.RadioButton radPen1;
        private System.Windows.Forms.Label lblSelectPen;
        private KnobControl.KnobControl knobFrequency;
        private GroupBoxEx.GroupBoxEx grpbxexFrequency;
        private System.Windows.Forms.Label lblFrequency;
        private System.Windows.Forms.Button btnSendFrequency;
        private System.Windows.Forms.Label lblFrequencyValue;
        private GroupBoxEx.GroupBoxEx grpbxexPower;
        private System.Windows.Forms.Label lblPowerValue;
        private System.Windows.Forms.Label lblPower;
        private KnobControl.KnobControl knobPower;
        private System.Windows.Forms.Button btnSendFreeData;
        private GroupBoxEx.GroupBoxEx grpbxexPedalStatus;
        private PictureBoxEx.PictureBoxEx picbxPedalUpDown;
        private GroupBoxEx.GroupBoxEx grpbxexCurrentCommand;
        private System.Windows.Forms.RichTextBox richtxtStatus;
        private System.Windows.Forms.Button btnClearStatus;
        private System.Windows.Forms.Label lblComPort;
        private System.Windows.Forms.ComboBox cmbPortName;
        private System.Windows.Forms.Button btnOpenPort;
        private System.Windows.Forms.CheckBox chkbxLineFeed;
        private GroupBoxEx.GroupBoxEx grpbxCommunicationSettings;
        private System.Windows.Forms.TextBox txtSendData;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Button btnPower;
        private System.Windows.Forms.Timer tmrCheckComPorts;
        private System.Windows.Forms.CheckBox chkbxSimulationPedal;
        private System.Windows.Forms.CheckBox chkbxStopLogging;
    }
}

