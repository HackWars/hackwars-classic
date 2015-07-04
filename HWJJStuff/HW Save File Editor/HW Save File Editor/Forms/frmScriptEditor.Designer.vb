<Global.Microsoft.VisualBasic.CompilerServices.DesignerGenerated()> _
Partial Class frmScriptEditor
    Inherits System.Windows.Forms.Form

    'Form overrides dispose to clean up the component list.
    <System.Diagnostics.DebuggerNonUserCode()> _
    Protected Overrides Sub Dispose(ByVal disposing As Boolean)
        Try
            If disposing AndAlso components IsNot Nothing Then
                components.Dispose()
            End If
        Finally
            MyBase.Dispose(disposing)
        End Try
    End Sub

    'Required by the Windows Form Designer
    Private components As System.ComponentModel.IContainer

    'NOTE: The following procedure is required by the Windows Form Designer
    'It can be modified using the Windows Form Designer.  
    'Do not modify it using the code editor.
    <System.Diagnostics.DebuggerStepThrough()> _
    Private Sub InitializeComponent()
        Me.components = New System.ComponentModel.Container
        Me.Label1 = New System.Windows.Forms.Label
        Me.cmdClose = New Oak_Underwriting_Library.JJButton
        Me.SyntaxDocument1 = New Puzzle.SourceCode.SyntaxDocument(Me.components)
        Me.TabControl1 = New System.Windows.Forms.TabControl
        Me.Panel1 = New System.Windows.Forms.Panel
        Me.Panel2 = New System.Windows.Forms.Panel
        Me.SyntaxDocument2 = New Puzzle.SourceCode.SyntaxDocument(Me.components)
        Me.SyntaxDocument3 = New Puzzle.SourceCode.SyntaxDocument(Me.components)
        Me.Panel1.SuspendLayout()
        Me.Panel2.SuspendLayout()
        Me.SuspendLayout()
        '
        'Label1
        '
        Me.Label1.AutoSize = True
        Me.Label1.ForeColor = System.Drawing.Color.FromArgb(CType(CType(132, Byte), Integer), CType(CType(161, Byte), Integer), CType(CType(198, Byte), Integer))
        Me.Label1.Location = New System.Drawing.Point(12, 9)
        Me.Label1.Name = "Label1"
        Me.Label1.Size = New System.Drawing.Size(168, 13)
        Me.Label1.TabIndex = 1
        Me.Label1.Text = "Creating and Editing coming soon."
        '
        'cmdClose
        '
        Me.cmdClose.ButtonStyle = Oak_Underwriting_Library.ButtonEnums.ButtonStyle.WindowsForm
        Me.cmdClose.Location = New System.Drawing.Point(113, 4)
        Me.cmdClose.Name = "cmdClose"
        Me.cmdClose.Size = New System.Drawing.Size(75, 23)
        Me.cmdClose.TabIndex = 2
        Me.cmdClose.Text = "Close"
        Me.cmdClose.UseVisualStyleBackColor = True
        '
        'SyntaxDocument1
        '
        Me.SyntaxDocument1.Lines = New String() {""}
        Me.SyntaxDocument1.MaxUndoBufferSize = 1000
        Me.SyntaxDocument1.Modified = False
        Me.SyntaxDocument1.UndoStep = 0
        '
        'TabControl1
        '
        Me.TabControl1.Dock = System.Windows.Forms.DockStyle.Fill
        Me.TabControl1.Location = New System.Drawing.Point(0, 34)
        Me.TabControl1.Name = "TabControl1"
        Me.TabControl1.SelectedIndex = 0
        Me.TabControl1.Size = New System.Drawing.Size(626, 412)
        Me.TabControl1.TabIndex = 4
        '
        'Panel1
        '
        Me.Panel1.BackColor = System.Drawing.Color.Transparent
        Me.Panel1.Controls.Add(Me.Panel2)
        Me.Panel1.Controls.Add(Me.Label1)
        Me.Panel1.Dock = System.Windows.Forms.DockStyle.Top
        Me.Panel1.Location = New System.Drawing.Point(0, 0)
        Me.Panel1.Name = "Panel1"
        Me.Panel1.Size = New System.Drawing.Size(626, 34)
        Me.Panel1.TabIndex = 5
        '
        'Panel2
        '
        Me.Panel2.Controls.Add(Me.cmdClose)
        Me.Panel2.Dock = System.Windows.Forms.DockStyle.Right
        Me.Panel2.Location = New System.Drawing.Point(426, 0)
        Me.Panel2.Name = "Panel2"
        Me.Panel2.Size = New System.Drawing.Size(200, 34)
        Me.Panel2.TabIndex = 2
        '
        'SyntaxDocument2
        '
        Me.SyntaxDocument2.Lines = New String() {""}
        Me.SyntaxDocument2.MaxUndoBufferSize = 1000
        Me.SyntaxDocument2.Modified = False
        Me.SyntaxDocument2.UndoStep = 0
        '
        'SyntaxDocument3
        '
        Me.SyntaxDocument3.Lines = New String() {""}
        Me.SyntaxDocument3.MaxUndoBufferSize = 1000
        Me.SyntaxDocument3.Modified = False
        Me.SyntaxDocument3.UndoStep = 0
        '
        'frmScriptEditor
        '
        Me.AutoScaleDimensions = New System.Drawing.SizeF(6.0!, 13.0!)
        Me.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font
        Me.BackColor = System.Drawing.SystemColors.Control
        Me.BackgroundImage = Global.HW_Save_File_Editor.My.Resources.Resources.background
        Me.BackgroundImageLayout = System.Windows.Forms.ImageLayout.Stretch
        Me.ClientSize = New System.Drawing.Size(626, 446)
        Me.Controls.Add(Me.TabControl1)
        Me.Controls.Add(Me.Panel1)
        Me.DoubleBuffered = True
        Me.Name = "frmScriptEditor"
        Me.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen
        Me.Text = "Script Editor"
        Me.Panel1.ResumeLayout(False)
        Me.Panel1.PerformLayout()
        Me.Panel2.ResumeLayout(False)
        Me.ResumeLayout(False)

    End Sub
    Friend WithEvents Label1 As System.Windows.Forms.Label
    Friend WithEvents cmdClose As Oak_Underwriting_Library.JJButton
    Friend WithEvents SyntaxDocument1 As Puzzle.SourceCode.SyntaxDocument
    Friend WithEvents TabControl1 As System.Windows.Forms.TabControl
    Friend WithEvents Panel1 As System.Windows.Forms.Panel
    Friend WithEvents Panel2 As System.Windows.Forms.Panel
    Friend WithEvents SyntaxDocument2 As Puzzle.SourceCode.SyntaxDocument
    Friend WithEvents SyntaxDocument3 As Puzzle.SourceCode.SyntaxDocument
End Class
