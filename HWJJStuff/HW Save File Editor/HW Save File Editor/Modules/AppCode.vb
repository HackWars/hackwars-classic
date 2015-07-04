Public Module AppCode

    Public Enum FileTypes
        BankingCompiled = 0
        BankingScript = 1
        AttackingCompiled = 2
        AttackingScript = 3
        WatchCompiled = 4
        WatchScript = 5
        FTPCompiled = 6
        FTPScript = 7
        Firewall = 8
        Text = 9
        CPU = 10
        HD = 11
        HTTP = 12
        Memory = 13
        Image = 14
        HTTPScript = 15
        Clue = 16
        Bounty = 17
        AGP = 18
        PCI = 19
        GameProject = 20
        Game = 21
        QuestGame = 22
        ShippingCompiled = 23
        ShippingScript = 24
        QuestItem = 25
        CommoditySlip = 26
        Challenge = 27
        NewFirewall = 28
    End Enum

    Public Function GetTypeString(ByVal Type As FileTypes) As String
        If Type = FileTypes.BankingCompiled Or Type = FileTypes.AttackingCompiled Or Type = FileTypes.WatchCompiled Or Type = FileTypes.FTPCompiled Or Type = FileTypes.HTTP Or Type = FileTypes.ShippingCompiled Then
            Return "compiled"
        ElseIf Type = FileTypes.BankingScript Or Type = FileTypes.AttackingScript Or Type = FileTypes.WatchScript Or Type = FileTypes.FTPScript Or Type = FileTypes.HTTPScript Or Type = FileTypes.ShippingScript Then
            Return "script"
        ElseIf Type = FileTypes.Text Then
            Return "text"
        ElseIf Type = FileTypes.Firewall Or Type = FileTypes.NewFirewall Then
            Return "firewall"
        ElseIf Type = FileTypes.Image Then
            Return "image"
        ElseIf Type = FileTypes.AGP Or Type = FileTypes.PCI Then
            Return "hardware"
        ElseIf Type = FileTypes.Game Then
            Return "game"
        ElseIf Type = FileTypes.GameProject Then
            Return "folder"
        ElseIf Type = FileTypes.CommoditySlip Then
            Return "commod"
        Else
            Return "new"
        End If
    End Function

    Public Function GetFileTypeString(ByVal Type As FileTypes)
        Select Case Type
            Case FileTypes.AGP
                Return "Hardware"
            Case FileTypes.AttackingCompiled
                Return "Compiled Attack Script"
            Case FileTypes.AttackingScript
                Return "Attack Script"
            Case FileTypes.BankingCompiled
                Return "Compiled Bank Script"
            Case FileTypes.BankingScript
                Return "Bank Script"
            Case FileTypes.Bounty
                Return "Bounty"
            Case FileTypes.Challenge
                Return "Challenge"
            Case FileTypes.Clue
                Return "Clue"
            Case FileTypes.CommoditySlip
                Return "Commodity Slip"
            Case FileTypes.CPU
                Return "CPU"
            Case FileTypes.Firewall
                Return "Firewall"
            Case FileTypes.FTPCompiled
                Return "Compiled FTP Script"
            Case FileTypes.FTPScript
                Return "FTP Script"
            Case FileTypes.Game
                Return "Compiled Game"
            Case FileTypes.GameProject
                Return "Game Project"
            Case FileTypes.HD
                Return "HD"
            Case FileTypes.HTTP
                Return "Compiled HTTP Script"
            Case FileTypes.HTTPScript
                Return "HTTP Script"
            Case FileTypes.Image
                Return "Image"
            Case FileTypes.Memory
                Return "Memory"
            Case FileTypes.NewFirewall
                Return "Firewall"
            Case FileTypes.PCI
                Return "PCI"
            Case FileTypes.QuestGame
                Return "Quest Game"
            Case FileTypes.QuestItem
                Return "Quest Item"
            Case FileTypes.ShippingCompiled
                Return "Compiled Redirect Script"
            Case FileTypes.ShippingScript
                Return "Redirect Script"
            Case FileTypes.Text
                Return "Text File"
            Case FileTypes.WatchCompiled
                Return "Compiled Watch Script"
            Case FileTypes.WatchScript
                Return "Watch Script"
            Case Else
                Return "Unknown Type"
        End Select
    End Function

End Module
