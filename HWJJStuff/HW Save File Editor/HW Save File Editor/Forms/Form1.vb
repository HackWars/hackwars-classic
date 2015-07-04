Imports MySql.Data.MySqlClient
Imports System.Xml

Public Class Form1

    Private Const ConnStr As String = "Server=localhost;Port=3306;Database=hackwars;Uid=root;Pwd=;"
    Private xml As XmlDocument
    Private RootDir As Directory
    Private CurrentDir As Directory = Nothing
    Private MySorter As ListViewColumnSorter

    Private Sub Form1_Load(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles MyBase.Load
        Dim MyConn As New MySqlConnection(ConnStr)
        Dim MyCmd As New MySqlCommand("Select * From `user` where ip = '152.009.3.113' limit 1", MyConn)
        Dim MyDr As MySqlDataReader

        MySorter = New ListViewColumnSorter()

        RootDir = New Directory("/")
        xml = New XmlDocument()

        Try
            MyConn.Open()

            MyDr = MyCmd.ExecuteReader()

            MyDr.Read()
            xml.LoadXml(GetXMLSaveFromByte(MyDr("stats")))

            ProcessFiles()

        Catch ex As Exception
            MsgBox(ex.Message)
        Finally
            MyConn.Close()
            MyConn.Dispose()
        End Try
    End Sub

    Private Sub ProcessFiles()
        Dim DirList As String() = Nothing
        Dim cDir As Directory = Nothing
        Dim MyFile As File = Nothing
        Dim MyFiles As XmlNodeList = xml.GetElementsByTagName("files")

        CurrentDir = RootDir

        For Each File As XmlElement In MyFiles(0).ChildNodes 'There should only be 1 <files></files>!
            If LCase(File.Name) = "directory" Then
                DirList = File.InnerText.Split("/")

                For Each s As String In DirList
                    If Not String.IsNullOrEmpty(s) Then
                        If cDir Is Nothing Then
                            cDir = New Directory(s, RootDir)
                        Else
                            cDir.SubDirs.Add(New Directory(s, cDir))
                        End If
                    End If
                Next

                RootDir.SubDirs.Add(cDir)
                cDir = Nothing

            ElseIf LCase(File.Name) = "file" Then
                DirList = File.Item("location").InnerText.Split("/")

                For Each s As String In DirList
                    If Not String.IsNullOrEmpty(s) Then
                        For Each dir As Directory In RootDir.SubDirs
                            If dir.Name = s Then
                                CurrentDir = dir

                                Exit For
                            End If
                        Next
                    Else
                        If CurrentDir Is Nothing Then CurrentDir = RootDir
                    End If
                Next

                'Now, lets load MyFile, and put it into the Dir.
                MyFile = New File(CurrentDir)

                MyFile.Name = File.Item("name").InnerText
                MyFile.Maker = File.Item("maker").InnerText
                MyFile.Location = File.Item("location").InnerText
                MyFile.Description = File.Item("description").InnerText
                MyFile.Quantity = File.Item("quantity").InnerText
                MyFile.Type = File.Item("type").InnerText
                MyFile.Price = File.Item("price").InnerText
                MyFile.CPU = File.Item("cpu").InnerText

                'We don't want to load the content of Games! Too much memory usage.
                If MyFile.Type <> FileTypes.Game Or MyFile.Type <> FileTypes.GameProject Then MyFile.Content = File.Item("content").InnerXml

                CurrentDir.Files.Add(MyFile)
            Else
                MsgBox("Oh an error?")
            End If
        Next

        CurrentDir = RootDir
        LoadDirsAndFiles(CurrentDir)
    End Sub

    Private Sub LoadDirsAndFiles(ByVal Dir As Directory)
        Dim Item As ListViewItem
        Dim SubItems() As ListViewItem.ListViewSubItem

        Me.ListView1.Items.Clear()

        For Each d As Directory In Dir.SubDirs
            Item = New ListViewItem(d.Name, "folder")
            SubItems = New ListViewItem.ListViewSubItem() {New ListViewItem.ListViewSubItem(Item, "Directory"), New ListViewItem.ListViewSubItem(Item, "A")}

            Item.SubItems.AddRange(SubItems)
            Me.ListView1.Items.Add(Item)
        Next

        For Each f As File In Dir.Files
            Item = New ListViewItem(f.Name, GetTypeString(f.Type))
            SubItems = New ListViewItem.ListViewSubItem() {New ListViewItem.ListViewSubItem(Item, GetFileTypeString(f.Type)), New ListViewItem.ListViewSubItem(Item, "B")} 'Maybe change this to the filetype?

            Item.SubItems.AddRange(SubItems)
            Me.ListView1.Items.Add(Item)
        Next

        Me.ListView1.AutoResizeColumns(ColumnHeaderAutoResizeStyle.HeaderSize)

        Me.ListView1.ListViewItemSorter = MySorter

        MySorter.SortColumn = 0
        MySorter.Order = SortOrder.Ascending

        Me.ListView1.Sort()

        Me.ListView1.ListViewItemSorter = MySorter

        MySorter.SortColumn = Me.ListView1.Columns.Count - 1
        MySorter.Order = SortOrder.Ascending

        Me.ListView1.Sort()
    End Sub

    Private Function GetXMLSaveFromByte(ByVal Data As Byte()) As String
        Return System.Text.Encoding.UTF8.GetString(Data)
    End Function

    Private Sub ListView1_ColumnClick(ByVal sender As Object, ByVal e As System.Windows.Forms.ColumnClickEventArgs) Handles ListView1.ColumnClick
        Me.ListView1.AutoResizeColumns(ColumnHeaderAutoResizeStyle.HeaderSize)

        Me.ListView1.ListViewItemSorter = MySorter

        MySorter.SortColumn = e.Column
        MySorter.Order = SortOrder.Ascending

        Me.ListView1.Sort()

        MySorter.SortColumn = Me.ListView1.Columns.Count - 1
        MySorter.Order = SortOrder.Ascending

        Me.ListView1.Sort()
    End Sub

    Private Sub ListView1_DoubleClick(ByVal sender As Object, ByVal e As System.EventArgs) Handles ListView1.DoubleClick
        FileDoubleClicked()
    End Sub

    Private Sub ListView1_KeyPress(ByVal sender As Object, ByVal e As System.Windows.Forms.KeyPressEventArgs) Handles ListView1.KeyPress
        If e.KeyChar = Chr(13) Then
            FileDoubleClicked()
        End If
    End Sub

    Private Sub FileDoubleClicked()
        For Each item As ListViewItem In Me.ListView1.SelectedItems
            If item.SubItems(1).Text = "Directory" Then
                CurrentDir = Nothing

                'A directory was clicked. Lets load that directory.
                For Each Dir As Directory In RootDir.SubDirs
                    If Dir.Name = item.SubItems(0).Text Then
                        CurrentDir = Dir

                        Exit For
                    End If
                Next

                If CurrentDir Is Nothing Then CurrentDir = RootDir

                LoadDirsAndFiles(CurrentDir)
            Else
                'A file was clicked. Lets open the file if we can.
                For Each f As File In CurrentDir.Files
                    If f.Name = item.SubItems(0).Text Then
                        Select Case f.Type
                            Case FileTypes.AttackingCompiled, FileTypes.AttackingScript, FileTypes.BankingCompiled, FileTypes.BankingScript, FileTypes.FTPCompiled, FileTypes.FTPScript, FileTypes.HTTP, FileTypes.HTTPScript, FileTypes.ShippingCompiled, FileTypes.ShippingScript, FileTypes.Text, FileTypes.WatchCompiled, FileTypes.WatchScript
                                frmScriptEditor.FileToEdit = f

                                frmScriptEditor.Show()
                        End Select
                    End If
                Next
            End If
        Next
    End Sub

    Private Sub cmdBack_Click(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles cmdBack.Click
        If CurrentDir IsNot Nothing Then
            If CurrentDir.Parent IsNot Nothing Then
                CurrentDir = CurrentDir.Parent

                LoadDirsAndFiles(CurrentDir)
            End If
        End If
    End Sub
End Class
