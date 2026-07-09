${inviter_name} has invited you to join ${organisation} on TAB.
-----
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <title>You're invited to Tab</title>
    <style>
      body, table, td, a { -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; }
      table, td { mso-table-lspace: 0pt; mso-table-rspace: 0pt; }
      img { -ms-interpolation-mode: bicubic; border: 0; outline: none; text-decoration: none; }
      body { margin: 0; padding: 0; background-color: #f4f6f8; font-family: sans-serif; }
      @media only screen and (max-width: 600px) {
        .email-container { width: 100% !important; border-radius: 0 !important; }
        .outer-pad { padding: 0 !important; }
        .header-cell { padding: 28px 24px !important; }
        .body-cell { padding: 32px 24px 24px 24px !important; }
        .divider-cell { padding: 0 24px !important; }
        .security-cell { padding: 20px 24px 32px 24px !important; }
        .footer-cell { padding: 20px 24px 0 24px !important; }
        .h2 { font-size: 20px !important; }
        .body-text { font-size: 15px !important; line-height: 24px !important; }
        .btn-table { width: 100% !important; }
        .btn-td { width: 100% !important; border-radius: 6px !important; }
        .btn {
          display: block !important;
          width: 100% !important;
          text-align: center !important;
          box-sizing: border-box !important;
          padding: 16px 24px !important;
        }
      }
    </style>
  </head>
  <body style="margin:0; padding:0; background-color:#f4f6f8;">

    <!-- Preheader (hidden preview text) -->
    <div style="display:none; max-height:0; overflow:hidden; mso-hide:all;">
      You've been invited to join Tab. Accept your invitation to get started.&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌
    </div>

    <!-- Outer wrapper -->
    <table role="presentation" width="100%" cellspacing="0" cellpadding="0" border="0" style="background-color:#f4f6f8;">
      <tr>
        <td class="outer-pad" align="center" style="padding: 40px 16px;">

          <!-- Email container -->
          <table class="email-container" role="presentation" width="600" cellspacing="0" cellpadding="0" border="0"
            style="max-width:600px; width:100%; background-color:#ffffff; border-radius:8px; overflow:hidden; box-shadow:0 2px 8px rgba(0,0,0,0.06);">

            <!-- Header -->
            <tr>
              <td class="header-cell" align="center" style="background-color:#123753; padding: 36px 40px;">
                <h1 style="margin:0; color:#ffffff; font-family:sans-serif; font-size:28px; font-weight:700; letter-spacing:-0.5px;">
                  Tab
                </h1>
              </td>
            </tr>

            <!-- Body -->
            <tr>
              <td class="body-cell" style="padding: 48px 40px 32px 40px;">
                <h2 class="h2" style="margin:0 0 16px 0; color:#123753; font-family:sans-serif; font-size:22px; font-weight:700; line-height:1.3;">
                  You've been invited!
                </h2>
                <p class="body-text" style="margin:0 0 16px 0; color:#444444; font-family:sans-serif; font-size:16px; line-height:26px;">
                  Hi there,
                </p>
                <p class="body-text" style="margin:0 0 24px 0; color:#444444; font-family:sans-serif; font-size:16px; line-height:26px;">
                  You've been invited to join <strong style="color:#123753;">Tab</strong>. Click the button below to accept your invitation and set up your account.
                </p>

                <!-- CTA Button -->
                <table class="btn-table" role="presentation" cellspacing="0" cellpadding="0" border="0" style="margin: 0 auto 32px auto;">
                  <tr>
                    <td class="btn-td" align="center" style="border-radius:6px; background-color:#123753;">
                      <a class="btn" href="${inviteLink}" target="_blank"
                        style="display:inline-block; padding:14px 36px; font-family:sans-serif; font-size:16px; font-weight:600; color:#ffffff; text-decoration:none; border-radius:6px; background-color:#123753; mso-padding-alt:0; letter-spacing:0.2px;">
                        Accept Invitation
                      </a>
                    </td>
                  </tr>
                </table>

                <p style="margin:0 0 8px 0; color:#888888; font-family:sans-serif; font-size:13px; line-height:22px;">
                  Or copy and paste this link into your browser:
                </p>
                <p style="margin:0; font-family:sans-serif; font-size:13px; line-height:22px;">
                  <a href="${inviteLink}" target="_blank" style="color:#123753; word-break:break-all; text-decoration:underline;">
                    ${inviteLink}
                  </a>
                </p>
              </td>
            </tr>

            <!-- Divider -->
            <tr>
              <td class="divider-cell" style="padding: 0 40px;">
                <hr style="border:none; border-top:1px solid #e8edf2; margin:0;" />
              </td>
            </tr>

            <!-- Security note -->
            <tr>
              <td class="security-cell" style="padding: 24px 40px 40px 40px;">
                <p style="margin:0; color:#aaaaaa; font-family:sans-serif; font-size:12px; line-height:20px;">
                  This invitation link is unique to you — please do not share it. If you didn't expect this invitation, you can safely ignore this email.
                </p>
              </td>
            </tr>

          </table>
          <!-- /Email container -->

          <!-- Footer -->
          <table role="presentation" width="600" cellspacing="0" cellpadding="0" border="0" style="max-width:600px; width:100%;">
            <tr>
              <td class="footer-cell" align="center" style="padding: 24px 16px 0 16px;">
                <p style="margin:0 0 8px 0; color:#aaaaaa; font-family:sans-serif; font-size:12px; line-height:20px;">
                  &copy; 2026 Tab. All rights reserved.
                </p>
                <div data-role="module-unsubscribe" class="module" role="module" data-type="unsubscribe"
                  style="color:#aaaaaa; font-size:12px; line-height:20px; text-align:center;"
                  data-muid="4e838cf3-9892-4a6d-94d6-170e474d21e5">
                  <p style="margin:0; font-size:12px; line-height:20px;">
                    <a class="Unsubscribe--unsubscribeLink" href="" target="_blank"
                      style="font-family:sans-serif; color:#aaaaaa; text-decoration:underline;">
                      Unsubscribe
                    </a>
                    &nbsp;&middot;&nbsp;
                    <a href="" target="_blank" class="Unsubscribe--unsubscribePreferences"
                      style="font-family:sans-serif; color:#aaaaaa; text-decoration:underline;">
                      Unsubscribe Preferences
                    </a>
                  </p>
                </div>
              </td>
            </tr>
          </table>

        </td>
      </tr>
    </table>

  </body>
</html>
