Public Class Directory
    Private _Parent As Directory
    Private _SubDirs As List(Of Directory)
    Private _Files As List(Of File)
    Private _Name As String = ""

    Public Property Parent() As Directory
        Get
            Return _Parent
        End Get
        Set(ByVal value As Directory)
            _Parent = value
        End Set
    End Property

    Public Property SubDirs() As List(Of Directory)
        Get
            Return _SubDirs
        End Get
        Set(ByVal value As List(Of Directory))
            _SubDirs = value
        End Set
    End Property

    Public Property Files() As List(Of File)
        Get
            Return _Files
        End Get
        Set(ByVal value As List(Of File))
            _Files = value
        End Set
    End Property

    Public Property Name() As String
        Get
            Return _Name
        End Get
        Set(ByVal value As String)
            _Name = value
        End Set
    End Property

    Public Sub New(ByVal Name As String)
        Me.Name = Name

        Me.SubDirs = New List(Of Directory)
        Me.Files = New List(Of File)
    End Sub

    Public Sub New(ByVal Name As String, ByVal Parent As Directory)
        Me.New(Name)

        Me.Parent = Parent
    End Sub
End Class
