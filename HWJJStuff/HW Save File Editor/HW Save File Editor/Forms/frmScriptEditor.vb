Imports System.IO
Imports System.Reflection
Imports System.Xml

Public Class frmScriptEditor

    Public FileToEdit As File

    Private Sub cmdClose_Click(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles cmdClose.Click
        Me.Close()
    End Sub

    Private Sub frmScriptEditor_FormClosed(ByVal sender As Object, ByVal e As System.Windows.Forms.FormClosedEventArgs) Handles Me.FormClosed
        Me.Dispose()
    End Sub

    Private Sub frmScriptEditor_Load(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles MyBase.Load
        Dim xml As New XmlDocument

        xml.LoadXml("<content>" & FileToEdit.Content & "</content>")

        Me.Text &= " - " & FileToEdit.Name

        Me.TabControl1.TabPages.Clear()

        If FileToEdit.Type <> FileTypes.Text Then
            Me.SetSyntaxFromEmbeddedResource(My.Resources.Hackwars)
        End If

        Select Case FileToEdit.Type
            Case FileTypes.AttackingCompiled, FileTypes.AttackingScript
                Me.TabControl1.TabPages.Add(AddNewTab("Initialize", Me.SyntaxDocument1))
                Me.TabControl1.TabPages.Add(AddNewTab("Finalize", Me.SyntaxDocument2))
                Me.TabControl1.TabPages.Add(AddNewTab("Continue", Me.SyntaxDocument3))


                Me.SyntaxDocument1.Text = xml.GetElementsByTagName("initialize").Item(0).InnerText.Replace(vbLf, vbCrLf) 'Cmds = initialize, finalize, continue
                Me.SyntaxDocument2.Text = xml.GetElementsByTagName("finalize").Item(0).InnerText.Replace(vbLf, vbCrLf)
                Me.SyntaxDocument3.Text = xml.GetElementsByTagName("continue").Item(0).InnerText.Replace(vbLf, vbCrLf)

            Case FileTypes.BankingCompiled, FileTypes.BankingScript
                Me.TabControl1.TabPages.Add(AddNewTab("Deposit", Me.SyntaxDocument1))
                Me.TabControl1.TabPages.Add(AddNewTab("Withdraw", Me.SyntaxDocument2))
                Me.TabControl1.TabPages.Add(AddNewTab("Transfer", Me.SyntaxDocument3))


                Me.SyntaxDocument1.Text = xml.GetElementsByTagName("deposit").Item(0).InnerText.Replace(vbLf, vbCrLf) 'Cmds = deposit, withdraw, transfer
                Me.SyntaxDocument2.Text = xml.GetElementsByTagName("withdraw").Item(0).InnerText.Replace(vbLf, vbCrLf)
                Me.SyntaxDocument3.Text = xml.GetElementsByTagName("transfer").Item(0).InnerText.Replace(vbLf, vbCrLf)

            Case FileTypes.FTPCompiled, FileTypes.FTPScript
                Me.TabControl1.TabPages.Add(AddNewTab("Put", Me.SyntaxDocument1))
                Me.TabControl1.TabPages.Add(AddNewTab("Get", Me.SyntaxDocument2))


                Me.SyntaxDocument1.Text = xml.GetElementsByTagName("put").Item(0).InnerText.Replace(vbLf, vbCrLf) 'Cmds = put, get
                Me.SyntaxDocument2.Text = xml.GetElementsByTagName("get").Item(0).InnerText.Replace(vbLf, vbCrLf)

            Case FileTypes.HTTP, FileTypes.HTTPScript
                Me.TabControl1.TabPages.Add(AddNewTab("Enter", Me.SyntaxDocument1))
                Me.TabControl1.TabPages.Add(AddNewTab("Exit", Me.SyntaxDocument2))
                Me.TabControl1.TabPages.Add(AddNewTab("Submit", Me.SyntaxDocument3))


                Me.SyntaxDocument1.Text = xml.GetElementsByTagName("enter").Item(0).InnerText.Replace(vbLf, vbCrLf) 'Cmds = enter, exit, submit
                Me.SyntaxDocument2.Text = xml.GetElementsByTagName("exit").Item(0).InnerText.Replace(vbLf, vbCrLf)
                Me.SyntaxDocument3.Text = xml.GetElementsByTagName("submit").Item(0).InnerText.Replace(vbLf, vbCrLf)

            Case FileTypes.ShippingCompiled, FileTypes.ShippingScript
                Me.TabControl1.TabPages.Add(AddNewTab("Initialize", Me.SyntaxDocument1))
                Me.TabControl1.TabPages.Add(AddNewTab("Finalize", Me.SyntaxDocument2))
                Me.TabControl1.TabPages.Add(AddNewTab("Continue", Me.SyntaxDocument3))


                Me.SyntaxDocument1.Text = xml.GetElementsByTagName("initialize").Item(0).InnerText.Replace(vbLf, vbCrLf) 'Cmds = initialize, finalize, continue
                Me.SyntaxDocument2.Text = xml.GetElementsByTagName("finalize").Item(0).InnerText.Replace(vbLf, vbCrLf)
                Me.SyntaxDocument3.Text = xml.GetElementsByTagName("continue").Item(0).InnerText.Replace(vbLf, vbCrLf)

            Case FileTypes.Text
                Me.TabControl1.TabPages.Add(AddNewTab("Content", Me.SyntaxDocument1))

                Me.SyntaxDocument1.Text = xml.GetElementsByTagName("data").Item(0).InnerText.Replace(vbLf, vbCrLf)

            Case FileTypes.WatchCompiled, FileTypes.WatchScript
                Me.TabControl1.TabPages.Add(AddNewTab("Fire", Me.SyntaxDocument1))

                Me.SyntaxDocument1.Text = xml.GetElementsByTagName("fire").Item(0).InnerText.Replace(vbLf, vbCrLf)
            Case Else
                Me.Close()
        End Select

    End Sub

    Private Function AddNewTab(ByVal Text As String, ByRef SyntaxDocument As Puzzle.SourceCode.SyntaxDocument) As TabPage
        Dim Tab As New TabPage(Text)
        Dim Syntax As New Puzzle.Windows.Forms.SyntaxBoxControl

        Syntax.Dock = DockStyle.Fill
        Syntax.Document = SyntaxDocument

        Tab.Controls.Add(Syntax)

        Return Tab
    End Function

    Public Sub SetSyntaxFromEmbeddedResource(ByVal Resource As Byte())
        Me.SyntaxDocument1.Parser.Init(Puzzle.SourceCode.Language.FromSyntaxXml(System.Text.Encoding.UTF8.GetString(Resource)))
        Me.SyntaxDocument2.Parser.Init(Puzzle.SourceCode.Language.FromSyntaxXml(System.Text.Encoding.UTF8.GetString(Resource)))
        Me.SyntaxDocument3.Parser.Init(Puzzle.SourceCode.Language.FromSyntaxXml(System.Text.Encoding.UTF8.GetString(Resource)))
    End Sub
End Class